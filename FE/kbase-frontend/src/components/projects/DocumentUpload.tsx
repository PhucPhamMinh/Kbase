"use client";

import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import * as yup from "yup";
import { api } from "@/lib/api";
import type { ApiResponse, DocumentItem, ProjectPermission } from "@/lib/types";

const MAX_FILE_SIZE = 10 * 1024 * 1024;

const schema = yup.object({
  title: yup.string().max(255).required("Title is required"),
  description: yup.string().max(5000).optional()
});

type FormValues = yup.InferType<typeof schema>;
type ReadUrlResponse = {
  documentId: number;
  url: string;
  expiresAt: string;
};

export function DocumentUpload({ projectId }: { projectId: number }) {
  const [file, setFile] = useState<File | null>(null);
  const [documents, setDocuments] = useState<DocumentItem[]>([]);
  const [permissions, setPermissions] = useState<ProjectPermission | null>(null);
  const [loadingDocuments, setLoadingDocuments] = useState(false);
  const [openingDocumentId, setOpeningDocumentId] = useState<number | null>(null);
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm<FormValues>({
    resolver: yupResolver(schema)
  });

  useEffect(() => {
    const loadDocuments = async () => {
      setLoadingDocuments(true);
      try {
        const permissionsResponse = await api.get<ApiResponse<ProjectPermission>>(`/api/projects/${projectId}/permissions/me`);
        const nextPermissions = permissionsResponse.data.result;
        setPermissions(nextPermissions);
        if (!nextPermissions.canRead) {
          setDocuments([]);
          return;
        }
        const response = await api.get<ApiResponse<DocumentItem[]>>("/api/documents", {
          params: { projectId }
        });
        setDocuments(response.data.result);
      } catch {
        setDocuments([]);
        setPermissions(null);
      } finally {
        setLoadingDocuments(false);
      }
    };

    loadDocuments();
  }, [projectId]);

  const chooseFile = (nextFile: File | null) => {
    if (!nextFile) {
      setFile(null);
      return;
    }
    if (nextFile.size > MAX_FILE_SIZE) {
      toast.error("File size must not exceed 10MB");
      setFile(null);
      return;
    }
    setFile(nextFile);
  };

  const onSubmit = async (values: FormValues) => {
    if (!file) {
      toast.error("Select a file before uploading");
      return;
    }
    try {
      const data = new FormData();
      data.append("projectId", String(projectId));
      data.append("title", values.title);
      if (values.description) {
        data.append("description", values.description);
      }
      data.append("file", file);
      const response = await api.post<ApiResponse<DocumentItem>>("/api/documents", data);
      setDocuments((current) => [response.data.result, ...current]);
      setFile(null);
      reset();
      toast.success("Document uploaded");
    } catch {
      return;
    }
  };

  const openDocument = async (documentId: number) => {
    if (openingDocumentId !== null) {
      return;
    }
    setOpeningDocumentId(documentId);
    try {
      const response = await api.get<ApiResponse<ReadUrlResponse>>(`/api/documents/${documentId}/read-url`);
      const readUrl = response.data.result.url;
      if (!readUrl) {
        toast.error("Document read URL is unavailable");
        return;
      }
      window.open(readUrl, "_blank");
    } catch {
      return;
    } finally {
      setOpeningDocumentId(null);
    }
  };

  const updateDocument = async (document: DocumentItem) => {
    const title = window.prompt("Document title", document.title);
    if (!title) {
      return;
    }
    const description = window.prompt("Document description", document.description || "") || "";
    try {
      const response = await api.put<ApiResponse<DocumentItem>>(`/api/documents/${document.documentId}`, {
        title,
        description
      });
      setDocuments((current) => current.map((item) => item.documentId === document.documentId ? response.data.result : item));
      toast.success("Document updated");
    } catch {
      return;
    }
  };

  const deleteDocument = async (documentId: number) => {
    if (!window.confirm("Delete this document?")) {
      return;
    }
    try {
      await api.delete(`/api/documents/${documentId}`);
      setDocuments((current) => current.filter((item) => item.documentId !== documentId));
      toast.success("Document deleted");
    } catch {
      return;
    }
  };

  return (
    <div className="mx-auto w-full rounded-2xl border border-gray-100 bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)] md:p-10">
      <div className="grid gap-12 xl:grid-cols-[460px_1fr]">
        <div className="flex flex-col">
          <div className="mb-8">
            <h2 className="text-3xl font-bold text-[#030391]">Upload Document</h2>
            <p className="mt-2 text-base text-gray-500">Add new files to your project workspace.</p>
          </div>

          {!permissions?.canAdd && (
            <div className="rounded-xl border border-gray-200 bg-gray-50 p-5 text-base font-medium text-gray-500">
              You can view this project, but you do not have permission to upload documents.
            </div>
          )}

          {permissions?.canAdd && <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
            <div>
              <label className="mb-2 block text-base font-semibold text-[#030391]">Document Title</label>
              <input
                type="text"
                placeholder="Project Specifications"
                className={`w-full rounded-xl border bg-gray-50 px-5 py-4 text-base text-[#030391] outline-none transition-all duration-200 focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20 ${errors.title ? "border-red-500" : "border-gray-200"}`}
                {...register("title")}
              />
              {errors.title && <span className="mt-1.5 block text-xs font-medium text-red-500">{errors.title.message}</span>}
            </div>

            <div>
              <label className="mb-2 block text-base font-semibold text-[#030391]">
                Description <span className="font-normal text-gray-400">(Optional)</span>
              </label>
              <textarea
                placeholder="Briefly describe the document contents."
                className={`min-h-[140px] w-full resize-y rounded-xl border bg-gray-50 px-5 py-4 text-base text-[#030391] outline-none transition-all duration-200 focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20 ${errors.description ? "border-red-500" : "border-gray-200"}`}
                {...register("description")}
              />
            </div>

            <div>
              <label className="mb-2 block text-base font-semibold text-[#030391]">File Attachment</label>
              <label className={`flex min-h-[180px] cursor-pointer flex-col items-center justify-center rounded-xl border-2 border-dashed transition-all duration-200 ${file ? "border-[#1488D8] bg-[#1488D8]/5" : "border-gray-300 bg-gray-50 hover:border-[#1488D8] hover:bg-white"}`}>
                <input className="hidden" type="file" onChange={(event) => chooseFile(event.target.files?.[0] || null)} />
                <div className="flex flex-col items-center p-4 text-center">
                  <span className="text-base font-medium text-gray-600">
                    {file ? file.name : "Click to browse"}
                  </span>
                  <span className="mt-2 text-sm text-gray-400">
                    {file ? `${(file.size / 1024 / 1024).toFixed(2)} MB` : "Maximum file size: 10MB"}
                  </span>
                </div>
              </label>
            </div>

            <button
              disabled={isSubmitting}
              className="mt-4 w-full rounded-xl bg-[#1488D8] px-5 py-4 text-base font-bold tracking-wide text-white transition-all duration-300 hover:bg-[#030391] hover:shadow-lg hover:shadow-[#030391]/30 focus:outline-none focus:ring-4 focus:ring-[#1488D8]/50 disabled:cursor-not-allowed disabled:bg-gray-400 disabled:shadow-none"
            >
              {isSubmitting ? "Uploading..." : "Upload Document"}
            </button>
          </form>}
        </div>

        <div className="flex min-h-[560px] flex-col rounded-2xl border border-gray-100 bg-gray-50 p-8">
          <div className="mb-8 flex items-center justify-between gap-3">
            <h3 className="text-2xl font-bold text-[#030391]">Project Documents</h3>
            <span className="rounded-full bg-[#1488D8]/10 px-4 py-2 text-sm font-bold text-[#1488D8]">
              {documents.length} {documents.length === 1 ? "file" : "files"}
            </span>
          </div>

          <div className="flex-1 overflow-y-auto">
            {loadingDocuments ? (
              <div className="rounded-xl border border-gray-200 bg-white p-8 text-center text-base text-gray-500">Loading documents...</div>
            ) : documents.length === 0 ? (
              <div className="flex h-full min-h-[320px] flex-col items-center justify-center rounded-xl border border-dashed border-gray-300 bg-white p-8 text-center">
                <p className="text-base font-medium text-gray-500">No documents uploaded yet</p>
                <p className="mt-2 text-sm text-gray-400">Uploaded project documents will appear here after reload.</p>
              </div>
            ) : (
              <div className="grid gap-4">
                {documents.map((document) => (
                  <div key={document.documentId} className="group flex items-start gap-5 rounded-xl border border-gray-200 bg-white p-5 shadow-sm transition-all hover:border-[#1488D8] hover:shadow-md">
                    <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg bg-[#1488D8]/10 text-base font-bold uppercase text-[#1488D8]">
                      {document.fileType.slice(0, 3)}
                    </div>
                    <div className="min-w-0 flex-1">
                      <p className="truncate text-base font-bold text-[#030391]">{document.title}</p>
                      <div className="mt-2 flex flex-wrap items-center gap-2 text-sm text-gray-500">
                        <span className="truncate">{document.fileName}</span>
                        <span>|</span>
                        <span className="shrink-0 font-medium text-[#1488D8]">{(document.fileSize / 1024 / 1024).toFixed(2)} MB</span>
                      </div>
                    </div>
                    <div className="flex shrink-0 flex-wrap justify-end gap-2">
                      {permissions?.canRead && (
                        <button
                          type="button"
                          disabled={openingDocumentId !== null}
                          onClick={() => openDocument(document.documentId)}
                          className="rounded-lg border border-[#1488D8] px-4 py-3 text-sm font-bold text-[#1488D8] transition-colors hover:bg-[#1488D8] hover:text-white disabled:cursor-not-allowed disabled:opacity-50"
                        >
                          {openingDocumentId === document.documentId ? "Opening" : "Read"}
                        </button>
                      )}
                      {permissions?.canModify && (
                        <button
                          type="button"
                          onClick={() => updateDocument(document)}
                          className="rounded-lg border border-gray-300 px-4 py-3 text-sm font-bold text-[#030391] transition-colors hover:border-[#030391] hover:bg-[#030391] hover:text-white"
                        >
                          Edit
                        </button>
                      )}
                      {permissions?.canDelete && (
                        <button
                          type="button"
                          onClick={() => deleteDocument(document.documentId)}
                          className="rounded-lg border border-red-300 px-4 py-3 text-sm font-bold text-red-600 transition-colors hover:bg-red-600 hover:text-white"
                        >
                          Delete
                        </button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
