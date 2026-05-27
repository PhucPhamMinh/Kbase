"use client";

import type { Project } from "@/lib/types";
import { DocumentUpload } from "@/components/projects/DocumentUpload";
import { ProjectMembers } from "@/components/projects/ProjectMembers";

export function ProjectDetail({ project }: { project: Project | null }) {
  if (!project) {
    return (
      <div className="flex h-full min-h-[400px] w-full flex-col items-center justify-center rounded-2xl border-2 border-dashed border-gray-200 bg-gray-50 p-8 text-center transition-all">
        <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-[#1488D8]/10 text-[#1488D8]">
          <svg className="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 002-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
          </svg>
        </div>
        <h2 className="text-xl font-bold text-[#030391]">No Project Selected</h2>
        <p className="mt-2 max-w-sm text-sm text-gray-500">
          Create a new project or select an existing one from the sidebar to manage its documents.
        </p>
      </div>
    );
  }

  return (
    <div className="flex w-full flex-col space-y-8">
      <header className="relative overflow-hidden rounded-2xl border border-gray-100 bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)]">
        <div className="absolute left-0 top-0 h-full w-1.5 bg-[#1488D8]"></div>
        <div className="pl-3">
          <div className="flex items-center gap-3">
            <h2 className="text-3xl font-bold text-[#030391]">{project.projectName}</h2>
            <span className="rounded-full bg-[#1488D8]/10 px-4 py-2 text-sm font-bold text-[#1488D8]">
              Workspace
            </span>
          </div>
          <p className="mt-3 text-base leading-relaxed text-gray-600">
            {project.description || "No description provided for this project."}
          </p>
        </div>
      </header>

      <div className="w-full">
        <ProjectMembers project={project} />
      </div>

      <div className="w-full">
        <DocumentUpload projectId={project.projectId} />
      </div>
    </div>
  );
}
