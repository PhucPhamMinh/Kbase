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
};
