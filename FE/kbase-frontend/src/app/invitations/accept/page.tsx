"use client";

import { Suspense, useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import toast from "react-hot-toast";
import { api } from "@/lib/api";
import type { ApiResponse, ProjectMember } from "@/lib/types";
import { loadCurrentUser } from "@/store/authSlice";
import { useAppDispatch, useAppSelector } from "@/store/hooks";

export default function AcceptInvitationPage() {
  return (
    <Suspense fallback={<InvitationShell message="Loading invitation..." />}>
      <AcceptInvitationContent />
    </Suspense>
  );
}

function AcceptInvitationContent() {
  const params = useSearchParams();
  const token = params.get("token");
  const dispatch = useAppDispatch();
  const user = useAppSelector((state) => state.auth.user);
  const loading = useAppSelector((state) => state.auth.loading);
  const [status, setStatus] = useState("Preparing invitation...");
  const [accepted, setAccepted] = useState(false);

  useEffect(() => {
    if (!user && localStorage.getItem("kbase_token")) {
      dispatch(loadCurrentUser());
    }
  }, [dispatch, user]);

  useEffect(() => {
    const acceptInvitation = async () => {
      if (!token) {
        setStatus("Invitation token is missing.");
        return;
      }
      if (!localStorage.getItem("kbase_token")) {
        setStatus("Sign in with the invited email address, then open this invitation link again.");
        return;
      }
      if (!user || loading || accepted) {
        return;
      }

      try {
        const response = await api.post<ApiResponse<ProjectMember>>(`/api/projects/invitations/${token}/accept`);
        setAccepted(true);
        setStatus(`Invitation accepted. You can now access project #${response.data.result.projectId}.`);
        toast.success("Invitation accepted");
      } catch {
        setStatus("Unable to accept this invitation.");
      }
    };

    acceptInvitation();
  }, [accepted, loading, token, user]);

  return <InvitationShell message={status} />;
}

function InvitationShell({ message }: { message: string }) {
  return (
    <main className="flex min-h-screen items-center justify-center bg-gray-50 px-4 py-12">
      <section className="w-full max-w-xl rounded-2xl border border-gray-100 bg-white p-8 text-center shadow-[0_8px_30px_rgb(0,0,0,0.08)]">
        <h1 className="text-3xl font-extrabold tracking-tight text-[#030391]">Project Invitation</h1>
        <p className="mt-4 text-base text-gray-500">{message}</p>
        <a
          href="/"
          className="mt-8 inline-flex rounded-xl bg-[#1488D8] px-6 py-4 text-base font-bold text-white transition-all hover:bg-[#030391]"
        >
          Back to KBase
        </a>
      </section>
    </main>
  );
}
