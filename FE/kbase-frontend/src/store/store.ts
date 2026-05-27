import { configureStore } from "@reduxjs/toolkit";
import authReducer from "@/store/authSlice";
import projectReducer from "@/store/projectSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    projects: projectReducer
  }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
