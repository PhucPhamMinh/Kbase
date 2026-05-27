"use client";

import { Provider } from "react-redux";
import { Toaster } from "react-hot-toast";
import { store } from "@/store/store";

export function Providers({ children }: { children: React.ReactNode }) {
  return (
    <Provider store={store}>
      {children}
      <Toaster
        position="top-right"
        toastOptions={{
          style: {
            background: "#ffffff",
            color: "#030391",
            borderRadius: "12px",
            boxShadow: "0 8px 30px rgba(0, 0, 0, 0.08)",
            border: "1px solid #f3f4f6",
            fontSize: "14px",
            fontWeight: "500"
          },
          success: {
            iconTheme: {
              primary: "#1488D8",
              secondary: "#ffffff"
            }
          },
          error: {
            iconTheme: {
              primary: "#ef4444",
              secondary: "#ffffff"
            }
          }
        }}
      />
    </Provider>
  );
}
