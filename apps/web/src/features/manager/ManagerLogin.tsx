import { useRef, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router';
import { ApiFailure, postJson } from '../../lib/api';

export function ManagerLogin() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const submitting = useRef(false);
  const navigate = useNavigate();
  const mutation = useMutation({
    mutationFn: () => postJson('/api/auth/login', { email, password }),
    retry: false, gcTime: 0, networkMode: 'always',
    onSuccess: () => { setPassword(''); navigate('/manager', { replace: true }); },
    onError: (error) => {
      setPassword('');
      if (error instanceof ApiFailure && error.uncertain) setMessage('Chưa rõ kết quả đăng nhập. Hãy kiểm tra trạng thái trước khi thử lại.');
      else if (error instanceof ApiFailure && error.code === 'RATE_LIMITED') setMessage('Bạn đã thử quá nhiều lần. Vui lòng chờ trước khi thử lại.');
      else setMessage('Email hoặc mật khẩu không đúng.');
    },
    onSettled: () => { submitting.current = false; },
  });
  function submit(event: React.SubmitEvent<HTMLFormElement>) {
    event.preventDefault();
    if (submitting.current) return;
    setMessage(''); submitting.current = true; mutation.mutate();
  }
  return <main className="mx-auto w-full max-w-md px-5 py-10">
    <p className="text-sm font-semibold text-teal-700">CapMoney</p>
    <h1 className="mt-3 text-2xl font-semibold">Đăng nhập tài khoản quản lý</h1>
    {message && <p role="alert" className="mt-5 rounded-xl bg-white p-4">{message}</p>}
    <form className="mt-6 space-y-5" onSubmit={submit} aria-busy={mutation.isPending}>
      <div><label htmlFor="manager-email" className="block font-medium">Email quản lý</label>
        <input id="manager-email" type="email" autoComplete="username" required value={email}
          onChange={(event) => setEmail(event.target.value)} disabled={mutation.isPending}
          className="mt-2 w-full rounded-lg border border-slate-400 bg-white p-3" /></div>
      <div><label htmlFor="manager-password" className="block font-medium">Mật khẩu</label>
        <input id="manager-password" type="password" autoComplete="current-password" required value={password}
          onChange={(event) => setPassword(event.target.value)} disabled={mutation.isPending}
          className="mt-2 w-full rounded-lg border border-slate-400 bg-white p-3" /></div>
      <button type="submit" disabled={mutation.isPending} className="w-full rounded-lg bg-teal-700 p-3 font-semibold text-white disabled:opacity-60">
        {mutation.isPending ? 'Đang đăng nhập…' : 'Đăng nhập'}
      </button>
    </form>
  </main>;
}
