import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router';
import './style.css';
import { SetupEntry } from './features/manager/SetupEntry';

function Placeholder() {
  return (
    <main className="mx-auto mt-12 max-w-xl rounded-2xl bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-semibold">CapMoney</h1>
      <p className="mt-4 text-slate-600">Đây là trang thử hạ tầng. Chức năng tài khoản và ví chưa được triển khai.</p>
    </main>
  );
}

createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Placeholder />} />
        <Route path="/manager/setup" element={<SetupEntry />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
);
