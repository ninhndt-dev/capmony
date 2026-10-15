import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router';
import './style.css';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SetupEntry } from './features/manager/SetupEntry';
import { ManagerLogin } from './features/manager/ManagerLogin';
import { ManagerHome } from './features/manager/ManagerHome';

const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false, staleTime: 0 }, mutations: { retry: false, gcTime: 0 } } });

function Placeholder() {
  return (
    <main className="mx-auto mt-12 max-w-xl rounded-2xl bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-semibold">CapMoney</h1>
      <p className="mt-4 text-slate-600">Tài khoản quản lý đã có thể thiết lập và đăng nhập. Chức năng ví đang được xây dựng.</p>
    </main>
  );
}

createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Placeholder />} />
        <Route path="/manager/setup" element={<SetupEntry />} />
        <Route path="/manager/login" element={<ManagerLogin />} />
        <Route path="/manager" element={<ManagerHome />} />
      </Routes>
    </BrowserRouter>
    </QueryClientProvider>
  </React.StrictMode>,
);
