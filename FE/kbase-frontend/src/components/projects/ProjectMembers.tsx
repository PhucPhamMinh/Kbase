"use client";

import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import * as yup from "yup";
import { api } from "@/lib/api";
import type { ApiResponse, Project, ProjectInvitation, ProjectMember } from "@/lib/types";
import { useAppSelector } from "@/store/hooks";

const inviteSchema = yup.object({
  email: yup.string().email("Enter a valid email").required("Email is required"),
  canRead: yup.boolean().required(),
  canAdd: yup.boolean().required(),
  canModify: yup.boolean().required(),
  canDelete: yup.boolean().required()
});

type InviteFormValues = yup.InferType<typeof inviteSchema>;

export function ProjectMembers({ project }: { project: Project }) {
  const user = useAppSelector((state) => state.auth.user);
  const isOwner = user?.userId === project.ownerId;
  const [members, setMembers] = useState<ProjectMember[]>([]);
  const [invitations, setInvitations] = useState<ProjectInvitation[]>([]);
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm<InviteFormValues>({
    resolver: yupResolver(inviteSchema),
    defaultValues: {
      canRead: true,
      canAdd: false,
      canModify: false,
      canDelete: false
    }
  });

  useEffect(() => {
    const loadMembers = async () => {
      setLoading(true);
      try {
        const membersResponse = await api.get<ApiResponse<ProjectMember[]>>(`/api/projects/${project.projectId}/members`);
        setMembers(membersResponse.data.result);
        if (isOwner) {
          const invitationsResponse = await api.get<ApiResponse<ProjectInvitation[]>>(`/api/projects/${project.projectId}/invitations`);
          setInvitations(invitationsResponse.data.result);
        } else {
          setInvitations([]);
        }
      } catch {
        setMembers([]);
        setInvitations([]);
      } finally {
        setLoading(false);
      }
    };

    loadMembers();
  }, [project.projectId, isOwner]);

  const inviteMember = async (values: InviteFormValues) => {
    try {
      const response = await api.post<ApiResponse<ProjectInvitation>>(`/api/projects/${project.projectId}/invitations`, values);
      setInvitations((current) => [response.data.result, ...current]);
      reset({ email: "", canRead: true, canAdd: false, canModify: false, canDelete: false });
      toast.success("Invitation email sent");
    } catch {
      return;
    }
  };

  return (
    <section className="grid gap-6 rounded-2xl border border-gray-100 bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)] xl:grid-cols-[420px_1fr]">
      <div>
        <h3 className="text-2xl font-bold text-[#030391]">Project Members</h3>
        <p className="mt-2 text-base text-gray-500">Manage access and project permissions.</p>

        {isOwner && (
          <form onSubmit={handleSubmit(inviteMember)} className="mt-6 space-y-5 rounded-2xl border border-gray-100 bg-gray-50 p-6">
            <div>
              <label className="mb-2 block text-base font-semibold text-[#030391]">Invite by email</label>
              <input
                type="email"
                placeholder="member@example.com"
                className={`w-full rounded-xl border bg-white px-5 py-4 text-base text-[#030391] outline-none transition-all focus:border-[#1488D8] focus:ring-4 focus:ring-[#1488D8]/20 ${errors.email ? "border-red-500" : "border-gray-200"}`}
                {...register("email")}
              />
              {errors.email && <span className="mt-1.5 block text-xs font-medium text-red-500">{errors.email.message}</span>}
            </div>

            <div className="grid grid-cols-2 gap-3">
              {[
                ["canRead", "Read"],
                ["canAdd", "Add"],
                ["canModify", "Modify"],
                ["canDelete", "Delete"]
              ].map(([field, label]) => (
                <label key={field} className="flex items-center gap-3 rounded-xl border border-gray-200 bg-white px-4 py-3 text-sm font-semibold text-[#030391]">
                  <input type="checkbox" className="h-4 w-4 accent-[#1488D8]" {...register(field as keyof InviteFormValues)} />
                  {label}
                </label>
              ))}
            </div>

            <button
              disabled={isSubmitting}
              className="w-full rounded-xl bg-[#1488D8] px-5 py-4 text-base font-bold tracking-wide text-white transition-all hover:bg-[#030391] disabled:cursor-not-allowed disabled:bg-gray-400"
            >
              {isSubmitting ? "Sending..." : "Send Invitation"}
            </button>
          </form>
        )}
      </div>

      <div className="grid gap-5">
        <div className="rounded-2xl border border-gray-100 bg-gray-50 p-6">
          <div className="mb-4 flex items-center justify-between">
            <h4 className="text-lg font-bold text-[#030391]">Active Members</h4>
            <span className="rounded-full bg-[#1488D8]/10 px-4 py-2 text-sm font-bold text-[#1488D8]">{members.length}</span>
          </div>
          {loading ? (
            <p className="text-base text-gray-500">Loading members...</p>
          ) : members.length === 0 ? (
            <p className="text-base text-gray-500">No members found.</p>
          ) : (
            <div className="grid gap-3">
              {members.map((member) => (
                <div key={`${member.projectId}-${member.userId}`} className="rounded-xl border border-gray-200 bg-white p-4">
                  <div className="flex items-center justify-between gap-3">
                    <div>
                      <p className="text-base font-bold text-[#030391]">User #{member.userId}</p>
                      <p className="mt-1 text-sm text-gray-500">{member.role}</p>
                    </div>
                    <PermissionBadges permissions={member} />
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {isOwner && (
          <div className="rounded-2xl border border-gray-100 bg-gray-50 p-6">
            <div className="mb-4 flex items-center justify-between">
              <h4 className="text-lg font-bold text-[#030391]">Invitations</h4>
              <span className="rounded-full bg-[#1488D8]/10 px-4 py-2 text-sm font-bold text-[#1488D8]">{invitations.length}</span>
            </div>
            {invitations.length === 0 ? (
              <p className="text-base text-gray-500">No invitations yet.</p>
            ) : (
              <div className="grid gap-3">
                {invitations.map((invitation) => (
                  <div key={invitation.invitationId} className="rounded-xl border border-gray-200 bg-white p-4">
                    <div className="flex flex-wrap items-center justify-between gap-3">
                      <div>
                        <p className="text-base font-bold text-[#030391]">{invitation.invitedEmail}</p>
                        <p className="mt-1 text-sm text-gray-500">Status: {invitation.status}</p>
                      </div>
                      <PermissionBadges permissions={invitation} />
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </section>
  );
}

function PermissionBadges({ permissions }: { permissions: Pick<ProjectMember, "canRead" | "canAdd" | "canModify" | "canDelete"> }) {
  const badges = [
    ["Read", permissions.canRead],
    ["Add", permissions.canAdd],
    ["Modify", permissions.canModify],
    ["Delete", permissions.canDelete]
  ];

  return (
    <div className="flex flex-wrap justify-end gap-2">
      {badges.map(([label, enabled]) => (
        <span
          key={String(label)}
          className={`rounded-full px-3 py-1 text-xs font-bold ${enabled ? "bg-[#1488D8]/10 text-[#1488D8]" : "bg-gray-100 text-gray-400"}`}
        >
          {label}
        </span>
      ))}
    </div>
  );
}
