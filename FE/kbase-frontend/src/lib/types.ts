export type ApiResponse<T> = {
  status: number;
  message: string;
  result: T;
  metadata: unknown;
  timestamp: string;
};

export type User = {
  userId: number;
  email: string;
  fullName: string;
  role: "ADMIN" | "OWNER" | "USER";
  active: boolean;
};

export type Project = {
  projectId: number;
  projectName: string;
  description?: string;
  ownerId: number;
  active: boolean;
};

export type ProjectMember = {
  projectId: number;
  userId: number;
  role: "OWNER" | "MEMBER" | "VIEWER";
  canRead: boolean;
  canAdd: boolean;
  canModify: boolean;
  canDelete: boolean;
  active: boolean;
};

export type ProjectInvitation = {
  invitationId: number;
  projectId: number;
  invitedEmail: string;
  invitedBy: number;
  status: "PENDING" | "ACCEPTED" | "EXPIRED";
  canRead: boolean;
  canAdd: boolean;
  canModify: boolean;
  canDelete: boolean;
  expiresAt: string;
  acceptedAt?: string;
};

export type ProjectPermission = {
  projectId: number;
  userId: number;
  owner: boolean;
  canRead: boolean;
  canAdd: boolean;
  canModify: boolean;
  canDelete: boolean;
};

export type DocumentItem = {
  documentId: number;
  projectId: number;
  uploadedBy: number;
  title: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  storagePath: string;
  mimeType: string;
  description?: string;
  deleted: boolean;
  deletedAt?: string;
  deleteAfter?: string;
};
