"use client";

import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import * as yup from "yup";
import { createProject, fetchProjects, selectProject } from "@/store/projectSlice";
import { useAppDispatch, useAppSelector } from "@/store/hooks";
import { ProjectDetail } from "@/components/projects/ProjectDetail";

const schema = yup.object({
  projectName: yup.string().max(255).required("Project name is required"),
  description: yup.string().max(5000).optional()
});

type FormValues = yup.InferType<typeof schema>;

export function ProjectDashboard() {
  const dispatch = useAppDispatch();
  const { items, selected, loading } = useAppSelector((state) => state.projects);
  const { register, handleSubmit, reset, formState: { errors } } = useForm<FormValues>({ resolver: yupResolver(schema) });

  useEffect(() => {
    dispatch(fetchProjects());
  }, [dispatch]);

  const onSubmit = async (values: FormValues) => {
    try {
      await dispatch(createProject(values)).unwrap();
      reset();
    } catch {
      return;
    }
  };

  return (
    <section className="mx-auto w-full max-w-[1720px] pt-4">
      <div className="grid items-start gap-8 xl:grid-cols-[430px_1fr]">
        
        {/* Left Sidebar: Create Form & Project List */}
        <aside className="flex flex-col gap-8">
          
          {/* Create Project Form */}
          <div className="rounded-2xl border border-gray-100 bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)]">
            <h2 className="mb-6 text-2xl font-bold text-[#030391]">New Project</h2>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
              {/* Project Name */}
              <div>
                <input 
                  type="text"
                  placeholder="Project Name" 
                  className={`w-full rounded-xl border bg-gray-50 px-5 py-4 text-base text-[#030391] outline-none transition-all duration-200
                    focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20
                    ${errors.projectName ? 'border-red-500' : 'border-gray-200'}`}
                  {...register("projectName")} 
                />
                {errors.projectName && (
                  <span className="mt-1.5 flex items-center text-xs font-medium text-red-500">
                    {errors.projectName.message}
                  </span>
                )}
              </div>

              {/* Description */}
              <div>
                <textarea 
                  placeholder="Short description..." 
                  className="min-h-[140px] w-full resize-y rounded-xl border border-gray-200 bg-gray-50 px-5 py-4 text-base text-[#030391] outline-none transition-all duration-200 focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20" 
                  {...register("description")} 
                />
              </div>

              <button 
                className="w-full rounded-xl bg-[#1488D8] px-5 py-4 text-base font-bold tracking-wide text-white transition-all duration-300 
                  hover:bg-[#030391] hover:shadow-lg hover:shadow-[#030391]/30 
                  focus:outline-none focus:ring-4 focus:ring-[#1488D8]/50"
              >
                Create Project
              </button>
            </form>
          </div>

          {/* Projects List */}
          <div className="flex max-h-[760px] flex-col rounded-2xl border border-gray-100 bg-white shadow-[0_8px_30px_rgb(0,0,0,0.08)]">
            <div className="border-b border-gray-100 p-6 pb-5">
              <h2 className="text-2xl font-bold text-[#030391]">Your Projects</h2>
              <p className="mt-2 text-sm text-gray-500">Select a project to view details</p>
            </div>
            
            <div className="flex-1 overflow-y-auto p-4">
              {loading ? (
                <div className="flex items-center justify-center py-8">
                  {/* Loading Spinner */}
                  <svg className="h-6 w-6 animate-spin text-[#1488D8]" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  <span className="ml-2 text-base font-medium text-gray-500">Loading projects...</span>
                </div>
              ) : items.length === 0 ? (
                <div className="py-10 text-center text-base text-gray-500">
                  No projects found. Create one above!
                </div>
              ) : (
                <div className="grid gap-3">
                  {items.map((project) => {
                    const isSelected = selected?.projectId === project.projectId;
                    return (
                      <button
                        key={project.projectId}
                        onClick={() => dispatch(selectProject(project))}
                        className={`group w-full rounded-xl border px-5 py-4 text-left transition-all duration-200
                          ${isSelected 
                            ? "border-[#1488D8] bg-[#1488D8]/5 shadow-sm" 
                            : "border-transparent bg-white hover:border-[#1488D8]/50 hover:bg-gray-50"
                          }`}
                      >
                        <div className="flex items-center justify-between">
                          <span className={`block truncate text-base font-bold ${isSelected ? "text-[#1488D8]" : "text-[#030391] group-hover:text-[#1488D8]"}`}>
                            {project.projectName}
                          </span>
                          {/* Active Indicator Dot */}
                          {isSelected && <span className="h-2 w-2 rounded-full bg-[#1488D8] shadow-[0_0_8px_#1488D8]"></span>}
                        </div>
                        <span className={`mt-2 block truncate text-sm ${isSelected ? "text-[#1488D8]/70" : "text-gray-500"}`}>
                          {project.description || "No description provided"}
                        </span>
                      </button>
                    );
                  })}
                </div>
              )}
            </div>
          </div>
        </aside>

        {/* Right Content: Project Detail Component */}
        <main className="min-h-[720px] w-full rounded-2xl border border-gray-100 bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)]">
          {selected ? (
            <ProjectDetail project={selected} />
          ) : (
            /* Empty state when no project is selected */
            <div className="flex h-full flex-col items-center justify-center text-center">
              <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-gray-50">
                <svg className="h-8 w-8 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 002-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
                </svg>
              </div>
              <h3 className="text-lg font-bold text-[#030391]">No Project Selected</h3>
              <p className="mt-2 text-sm text-gray-500">Choose a project from the sidebar or create a new one to get started.</p>
            </div>
          )}
        </main>
        
      </div>
    </section>
  );
}
