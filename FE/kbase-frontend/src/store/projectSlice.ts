import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "@/lib/api";
import type { ApiResponse, Project } from "@/lib/types";

type ProjectState = {
  items: Project[];
  selected: Project | null;
  loading: boolean;
};

const initialState: ProjectState = {
  items: [],
  selected: null,
  loading: false
};

export const fetchProjects = createAsyncThunk("projects/fetch", async () => {
  const response = await api.get<ApiResponse<Project[]>>("/api/projects");
  return response.data.result;
});

export const createProject = createAsyncThunk("projects/create", async (payload: { projectName: string; description?: string }) => {
  const response = await api.post<ApiResponse<Project>>("/api/projects", payload);
  return response.data.result;
});

const projectSlice = createSlice({
  name: "projects",
  initialState,
  reducers: {
    selectProject(state, action) {
      state.selected = action.payload;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProjects.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchProjects.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
        state.selected = action.payload[0] || null;
      })
      .addCase(fetchProjects.rejected, (state) => {
        state.loading = false;
      })
      .addCase(createProject.fulfilled, (state, action) => {
        state.items.unshift(action.payload);
        state.selected = action.payload;
      });
  }
});

export const { selectProject } = projectSlice.actions;
export default projectSlice.reducer;
