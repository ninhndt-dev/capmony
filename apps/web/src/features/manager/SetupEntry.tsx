import { useState } from 'react';

// Capture before React mounts; StrictMode must not read an already-cleared fragment.
function takeSetupToken(): string | null {
  if (window.location.pathname !== '/manager/setup') return null;
  const fragment = window.location.hash;
  window.history.replaceState(null, '', window.location.pathname + window.location.search);
  const params = new URLSearchParams(fragment.slice(1));
  const tokens = params.getAll('token');
  return tokens.length === 1 && /^[A-Za-z0-9_-]{43}$/.test(tokens[0]) ? tokens[0] : null;
}

const initialToken = takeSetupToken();

export function SetupEntry() {
  const [password, setPassword] = useState('');
  const [confirmation, setConfirmation] = useState('');

  return (
    <main className="mx-auto w-full max-w-md px-5 py-10">
      <p className="text-sm font-semibold text-teal-700">CapMoney</p>
      <h1 className="mt-3 text-2xl font-semibold">Thiết lập tài khoản quản lý</h1>
      {!initialToken ? (
        <p role="status" className="mt-5 rounded-xl bg-white p-4">
          Hãy mở lại liên kết trong email thiết lập. Trang này không giữ liên kết sau khi tải lại.
        </p>
      ) : (
        <>
          <p className="mt-4 text-slate-600">
            Nhập mật khẩu mới và xác nhận lại. Liên kết chỉ được kiểm tra khi hoàn tất thiết lập.
          </p>
          <form className="mt-6 space-y-5" onSubmit={(event) => event.preventDefault()}>
            <div>
              <label htmlFor="setup-password" className="block font-medium">Mật khẩu mới</label>
              <input id="setup-password" type="password" autoComplete="new-password"
                value={password} onChange={(event) => setPassword(event.target.value)}
                aria-describedby="password-policy" className="mt-2 w-full rounded-lg border border-slate-400 bg-white p-3" />
              <p id="password-policy" className="mt-2 text-sm text-slate-600">Từ 12 đến 72 byte UTF-8.</p>
            </div>
            <div>
              <label htmlFor="setup-confirmation" className="block font-medium">Xác nhận mật khẩu</label>
              <input id="setup-confirmation" type="password" autoComplete="new-password"
                value={confirmation} onChange={(event) => setConfirmation(event.target.value)}
                className="mt-2 w-full rounded-lg border border-slate-400 bg-white p-3" />
            </div>
            <p role="status" className="text-sm text-slate-600">
              Bước hoàn tất thiết lập đang được triển khai. Chưa có tài khoản nào được tạo từ biểu mẫu này.
            </p>
            <button type="submit" disabled className="w-full rounded-lg bg-slate-200 p-3 font-semibold text-slate-600">
              Hoàn tất thiết lập
            </button>
          </form>
        </>
      )}
    </main>
  );
}
