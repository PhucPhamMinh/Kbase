import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "@/lib/api";
import type { ApiResponse, User } from "@/lib/types";

type LoginPayload = { email: string; password: string };
type RegisterPayload = { email: string; password: string; fullName: string; role: "ADMIN" | "OWNER" | "USER" };
type AuthResult = { token: string; user: User };

type AuthState = {
  user: User | null;
  token: string | null;
  loading: boolean;
};

const initialState: AuthState = {
  user: null,
  token: null,
  loading: false
};

export const login = createAsyncThunk("auth/login", async (payload: LoginPayload) => {
  const response = await api.post<ApiResponse<AuthResult>>("/api/auth/login", payload);
  return response.data.result;
});

export const register = createAsyncThunk("auth/register", async (payload: RegisterPayload) => {
  const response = await api.post<ApiResponse<User>>("/api/auth/register", payload);
  return response.data.result;
});

export const loadCurrentUser = createAsyncThunk("auth/me", async () => {
  const token = localStorage.getItem("kbase_token");
  if (!token) {
    throw new Error("Missing token");
  }
  const response = await api.get<ApiResponse<User>>("/api/users/me");
  return { token, user: response.data.result };
});

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logout(state) {
      state.user = null;
      state.token = null;
      localStorage.removeItem("kbase_token");
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.loading = true;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.user;
        state.token = action.payload.token;
        localStorage.setItem("kbase_token", action.payload.token);
      })
      .addCase(login.rejected, (state) => {
        state.loading = false;
      })
      .addCase(register.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(loadCurrentUser.pending, (state) => {
        state.loading = true;
      })
      .addCase(loadCurrentUser.fulfilled, (state, action) => {
        state.loading = false;
        state.token = action.payload.token;
        state.user = action.payload.user;
      })
      .addCase(loadCurrentUser.rejected, (state) => {
        state.loading = false;
        state.token = null;
        state.user = null;
        localStorage.removeItem("kbase_token");
      });
  }
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
