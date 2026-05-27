"use client";

import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import * as yup from "yup";
import { login } from "@/store/authSlice";
import { useAppDispatch, useAppSelector } from "@/store/hooks";

const schema = yup.object({
  email: yup.string().email("Enter a valid email").required("Email is required"),
  password: yup.string().required("Password is required")
});

type FormValues = yup.InferType<typeof schema>;

export function LoginForm() {
  const dispatch = useAppDispatch();
  const loading = useAppSelector((state) => state.auth.loading);
  const { register, handleSubmit, formState: { errors } } = useForm<FormValues>({ resolver: yupResolver(schema) });

  const onSubmit = async (values: FormValues) => {
    try {
      await dispatch(login(values)).unwrap();
      toast.success("Logged in");
    } catch {
      return;
    }
  };

  return (
    <div className="flex w-full justify-center">
      <form 
        onSubmit={handleSubmit(onSubmit)} 
        className="w-full max-w-md rounded-2xl bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)] border border-gray-100"
      >
        {/* Header Section */}
        <div className="mb-8 text-center">
          <h2 className="text-3xl font-bold text-[#030391]">Welcome Back</h2>
          <p className="mt-2 text-sm text-gray-500">Please enter your details to sign in.</p>
        </div>

        <div className="space-y-5">
          {/* Email Field */}
          <div>
            <label className="mb-1.5 block text-sm font-semibold text-[#030391]">
              Email Address
            </label>
            <input 
              type="email"
              placeholder="you@example.com"
              className={`w-full rounded-xl border bg-gray-50 px-4 py-3 text-sm text-[#030391] outline-none transition-all duration-200
                focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20
                ${errors.email ? 'border-red-500' : 'border-gray-200'}`}
              {...register("email")} 
            />
            {errors.email && (
              <span className="mt-1.5 flex items-center text-xs font-medium text-red-500">
                {errors.email.message}
              </span>
            )}
          </div>

          {/* Password Field */}
          <div>
            <label className="mb-1.5 block text-sm font-semibold text-[#030391]">
              Password
            </label>
            <input 
              type="password" 
              placeholder="••••••••"
              className={`w-full rounded-xl border bg-gray-50 px-4 py-3 text-sm text-[#030391] outline-none transition-all duration-200
                focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20
                ${errors.password ? 'border-red-500' : 'border-gray-200'}`}
              {...register("password")} 
            />
            {errors.password && (
              <span className="mt-1.5 flex items-center text-xs font-medium text-red-500">
                {errors.password.message}
              </span>
            )}
          </div>
        </div>

        {/* Submit Button */}
        <button 
          disabled={loading} 
          className="mt-8 w-full rounded-xl bg-[#1488D8] px-4 py-3.5 text-sm font-bold tracking-wide text-white transition-all duration-300 
            hover:bg-[#030391] hover:shadow-lg hover:shadow-[#030391]/30 
            focus:outline-none focus:ring-4 focus:ring-[#1488D8]/50 
            disabled:cursor-not-allowed disabled:bg-gray-400 disabled:shadow-none"
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>
      </form>
    </div>
  );
}