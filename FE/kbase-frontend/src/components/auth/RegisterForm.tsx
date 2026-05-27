"use client";

import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import * as yup from "yup";
import { register as registerUser } from "@/store/authSlice";
import { useAppDispatch } from "@/store/hooks";

const schema = yup.object({
  email: yup.string().email("Enter a valid email").required("Email is required"),
  password: yup.string().min(8, "Use at least 8 characters").required("Password is required"),
  fullName: yup.string().max(255).required("Full name is required"),
  role: yup.mixed<"ADMIN" | "OWNER" | "USER">().oneOf(["ADMIN", "OWNER", "USER"]).required()
});

type FormValues = yup.InferType<typeof schema>;

export function RegisterForm() {
  const dispatch = useAppDispatch();
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm<FormValues>({
    resolver: yupResolver(schema),
    defaultValues: { role: "USER" }
  });

  const onSubmit = async (values: FormValues) => {
    try {
      await dispatch(registerUser(values)).unwrap();
      reset({ role: "USER", email: "", password: "", fullName: "" });
      toast.success("Account created");
    } catch {
      return;
    }
  };

  return (
    <div className="flex w-full flex-col items-center justify-center">
      <form 
        onSubmit={handleSubmit(onSubmit)} 
        className="w-full max-w-md rounded-2xl bg-white p-8 shadow-[0_8px_30px_rgb(0,0,0,0.08)] border border-gray-100"
      >
        {/* Header Section */}
        <div className="mb-8 text-center">
          <h2 className="text-3xl font-bold text-[#030391]">Create Account</h2>
          <p className="mt-2 text-sm text-gray-500">Join us and start your journey.</p>
        </div>

        <div className="space-y-5">
          {/* Full Name Field */}
          <div>
            <label className="mb-1.5 block text-sm font-semibold text-[#030391]">
              Full Name
            </label>
            <input 
              type="text"
              placeholder="John Doe"
              className={`w-full rounded-xl border bg-gray-50 px-4 py-3 text-sm text-[#030391] outline-none transition-all duration-200
                focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20
                ${errors.fullName ? 'border-red-500' : 'border-gray-200'}`}
              {...register("fullName")} 
            />
            {errors.fullName && (
              <span className="mt-1.5 flex items-center text-xs font-medium text-red-500">
                {errors.fullName.message}
              </span>
            )}
          </div>

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

          {/* Role Select Field */}
          <div>
            <label className="mb-1.5 block text-sm font-semibold text-[#030391]">
              Account Role
            </label>
            <select 
              className="w-full appearance-none rounded-xl border border-gray-200 bg-gray-50 px-4 py-3 text-sm text-[#030391] outline-none transition-all duration-200 hover:cursor-pointer focus:border-[#1488D8] focus:bg-white focus:ring-4 focus:ring-[#1488D8]/20"
              {...register("role")}
            >
              <option value="USER">User</option>
              <option value="OWNER">Owner</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
        </div>

        {/* Submit Button */}
        <button 
          disabled={isSubmitting} 
          className="mt-8 w-full rounded-xl bg-[#1488D8] px-4 py-3.5 text-sm font-bold tracking-wide text-white transition-all duration-300 
            hover:bg-[#030391] hover:shadow-lg hover:shadow-[#030391]/30 
            focus:outline-none focus:ring-4 focus:ring-[#1488D8]/50 
            disabled:cursor-not-allowed disabled:bg-gray-400 disabled:shadow-none"
        >
          {isSubmitting ? "Creating account..." : "Create Account"}
        </button>

        {/* Navigation to Login Page */}
        {/* <p className="mt-6 text-center text-sm text-gray-500">
          Already have an account?{" "}
          <a 
            href="/login" 
            className="font-semibold text-[#1488D8] transition-colors hover:text-[#030391] hover:underline"
          >
            Sign in
          </a>
        </p> */}
      </form>
    </div>
  );
}