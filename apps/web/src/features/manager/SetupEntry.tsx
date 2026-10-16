import { useRef, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Link, useNavigate } from 'react-router';
import { ApiFailure, postJson } from '../../lib/api';

// Clear before React mounts, including StrictMode's repeated initial render.
function takeSetupToken(): string | null {
  if (window.location.pathname !== '/manager/setup') return null;
  const fragment = window.location.hash;
  window.history.replaceState(null, '', window.location.pathname + window.location.search);
  const tokens = new URLSearchParams(fragment.slice(1)).getAll('token');
  return tokens.length === 1 && /^[A-Za-z0-9_-]{43}$/.test(tokens[0]) ? tokens[0] : null;
}
let capturedToken = takeSetupToken();

export function SetupEntry() {
  const [token, setToken] = useState(capturedToken);
  const [password, setPassword] = useState('');
  const [confirmation, setConfirmation] = useState('');
  const [message, setMessage] = useState('');
  const [uncertain, setUncertain] = useState(false);
  const submitting = useRef(false);
  const navigate = useNavigate();
  const mutation = useMutation({
    // Credentials stay in this tab's memory, never in mutation variables or persisted cache.
    mutationFn: () => postJson('/api/manager/setup', { token, password, passwordConfirmation: confirmation }),
    retry: false, gcTime: 0, networkMode: 'always',
    onSuccess: () => {
      capturedToken = null; setToken(null); setPassword(''); setConfirmation('');
      navigate('/manager/login', { replace: true, state: { setupComplete: true } });
    },
    onError: (error) => {
      if (error instanceof ApiFailure && error.uncertain) {
        capturedToken = null; setToken(null); setPassword(''); setConfirmation(''); setUncertain(true);
        setMessage('Chưa rõ kết quả thiết lập. Không gửi lại biểu mẫu. Hãy thử đăng nhập bằng mật khẩu vừa đặt.');
      } else if (error instanceof ApiFailure && error.code === 'SETUP_LINK_UNAVAILABLE') {
        setMessage('Liên kết không còn dùng được. Nếu bạn đã hoàn tất trước đó, hãy thử đăng nhập.');
      } else if (error instanceof ApiFailure && error.code === 'PASSWORD_INVALID') {
        setMessage('Kiểm tra mật khẩu và xác nhận: từ 12 đến 72 byte UTF-8, không có ký tự NUL.');
      } else if (error instanceof ApiFailure && error.code === 'RATE_LIMITED') {
        setMessage('Bạn đã thử quá nhiều lần. Vui lòng chờ trước khi thử lại.');
      } else {
        setMessage('Chưa thể thực hiện yêu cầu. Kiểm tra kết nối rồi thử lại.');
      }
    },
    onSettled: () => { submitting.current = false; },
  });

  function submit(event: React.SubmitEvent<HTMLFormElement>) {
    event.preventDefault();
    if (submitting.current || uncertain || !token) return;
    const bytes = new TextEncoder().encode(password).length;
    if (bytes < 12 || bytes > 72 || password.includes('\0') || password !== confirmation) {
      setMessage('Mật khẩu cần từ 12 đến 72 byte UTF-8, không có ký tự NUL; xác nhận phải khớp.');
      return;
    }
    setMessage(''); submitting.current = true; mutation.mutate();
  }

  return (
    <main className="mx-auto w-full max-w-md px-5 py-10">
      <p className="text-sm font-semibold text-teal-700">CapMoney</p>
      <h1 className="mt-3 text-2xl font-semibold">Thiết lập tài khoản quản lý</h1>
      {message && <p role="alert" className="mt-5 rounded-xl bg-white p-4">{message}</p>}
      {!token ? (
        <div className="mt-5 space-y-4">
          {!uncertain && <p role="status">Hãy mở lại liên kết trong email thiết lập. Trang này không giữ liên kết sau khi tải lại.</p>}
          {uncertain && <Link className="inline-block font-semibold text-teal-700 underline" to="/manager/login">Thử đăng nhập</Link>}
        </div>
      ) : (
        <>
          <p className="mt-4 text-slate-600">Nhập mật khẩu mới và xác nhận lại. Liên kết được kiểm tra khi bạn hoàn tất thiết lập.</p>
          <form className="mt-6 space-y-5" onSubmit={submit} aria-busy={mutation.isPending}>
            <div>
              <label htmlFor="setup-password" className="block font-medium">Mật khẩu mới</label>
              <input id="setup-password" type="password" autoComplete="new-password" required
                value={password} onChange={(event) => setPassword(event.target.value)} disabled={mutation.isPending}
                aria-describedby="password-policy" className="mt-2 w-full rounded-lg border border-slate-400 bg-white p-3" />
              <p id="password-policy" className="mt-2 text-sm text-slate-600">Từ 12 đến 72 byte UTF-8.</p>
            </div>
            <div>
              <label htmlFor="setup-confirmation" className="block font-medium">Xác nhận mật khẩu</label>
              <input id="setup-confirmation" type="password" autoComplete="new-password" required
                value={confirmation} onChange={(event) => setConfirmation(event.target.value)} disabled={mutation.isPending}
                className="mt-2 w-full rounded-lg border border-slate-400 bg-white p-3" />
            </div>
            <button type="submit" disabled={mutation.isPending} className="w-full rounded-lg bg-teal-700 p-3 font-semibold text-white disabled:opacity-60">
              {mutation.isPending ? 'Đang thiết lập…' : 'Hoàn tất thiết lập'}
            </button>
          </form>
        </>
      )}
    </main>
  );
}
