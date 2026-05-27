"use client";

import { useEffect, useState } from "react";
import { LoginForm } from "@/components/auth/LoginForm";
import { RegisterForm } from "@/components/auth/RegisterForm";
import { ProjectDashboard } from "@/components/projects/ProjectDashboard";
import { loadCurrentUser, logout } from "@/store/authSlice";
import { useAppDispatch, useAppSelector } from "@/store/hooks";

export function HomeShell() {
  const dispatch = useAppDispatch();
  const user = useAppSelector((state) => state.auth.user);
  const loading = useAppSelector((state) => state.auth.loading);
  const [authMode, setAuthMode] = useState<"login" | "register">("login");

  useEffect(() => {
    if (!user && localStorage.getItem("kbase_token")) {
      dispatch(loadCurrentUser());
    }
  }, [dispatch, user]);

  if (!user && loading) {
    return (
      <main className="flex min-h-screen items-center justify-center bg-gray-50 px-4 py-12">
        <div className="w-full max-w-md rounded-xl border border-gray-200 bg-white p-6 text-center text-sm text-gray-500 shadow-sm">
          Loading workspace...
        </div>
      </main>
    );
  }

  if (!user) {
    return (
      <main className="flex min-h-screen flex-col items-center justify-center bg-gray-50 px-4 py-12">
        <div className="w-full max-w-md">
          <header className="mb-8 text-center">
            <h1 className="text-4xl font-extrabold tracking-tight text-[#030391]">KBase</h1>
            <p className="mt-2 text-sm text-gray-500">Project knowledge base and document management.</p>
          </header>

          {authMode === "login" ? (
            <div>
              <LoginForm />
              <p className="mt-6 text-center text-sm text-gray-500">
                Don't have an account?{" "}
                <button
                  onClick={() => setAuthMode("register")}
                  className="font-semibold text-[#1488D8] transition-colors hover:text-[#030391] hover:underline"
                >
                  Create one
                </button>
              </p>
            </div>
          ) : (
            <div>
              <RegisterForm />
              <p className="mt-6 text-center text-sm text-gray-500">
                Already have an account?{" "}
                <button
                  onClick={() => setAuthMode("login")}
                  className="font-semibold text-[#1488D8] transition-colors hover:text-[#030391] hover:underline"
                >
                  Sign in
                </button>
              </p>
            </div>
          )}
        </div>
      </main>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="sticky top-0 z-20 border-b border-gray-200 bg-white shadow-sm">
        <div className="mx-auto flex max-w-[1720px] items-center justify-between px-6 py-5 sm:px-8 lg:px-10">
          <div className="flex items-center gap-4">
            <h1 className="text-3xl font-extrabold tracking-tight text-[#030391]">KBase</h1>
            <div className="hidden h-6 w-px bg-gray-300 sm:block"></div>
            <p className="hidden text-base font-medium text-gray-500 sm:block">
              Welcome back, <span className="font-bold text-[#1488D8]">{user.fullName}</span>
            </p>
          </div>
          <div className="flex items-center gap-4">
            <p className="text-base font-medium text-[#1488D8] sm:hidden">{user.fullName}</p>
            <button
              onClick={() => dispatch(logout())}
              className="rounded-xl border border-gray-200 bg-white px-5 py-3 text-base font-bold text-gray-600 transition-all duration-200 hover:border-red-500 hover:bg-red-50 hover:text-red-600 focus:outline-none focus:ring-4 focus:ring-red-500/10"
            >
              Sign out
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-[1720px] p-6 sm:p-8 lg:p-10">
        <ProjectDashboard />
      </main>
    </div>
  );
}
