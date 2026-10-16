import { useEffect, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router';
import { ApiFailure, managerMe, postJson, refreshResultUnknown } from '../../lib/api';

export function ManagerHome() {
  const [email, setEmail] = useState<string | null>(null);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();
  useEffect(() => {
    let active = true;
    async function load() {
      try {
        let me;
        try { me = await managerMe(); }
        catch (error) {
          if (!(error instanceof ApiFailure) || error.code !== 'AUTH_REQUIRED') throw error;
          if (!active) return;
          if (refreshResultUnknown()) {
            setMessage('Lần gia hạn phiên trước chưa rõ kết quả. Hãy đăng nhập lại; ứng dụng sẽ không tự gửi lại yêu cầu cũ.');
            return;
          }
          await postJson('/api/auth/refresh', {});
          me = await managerMe();
        }
        if (active) setEmail(me.email);
      } catch (error) {
        if (!active) return;
        if (error instanceof ApiFailure && (error.code === 'AUTH_REQUIRED' || error.code === 'REFRESH_REUSED')) {
          navigate('/manager/login', { replace: true });
        } else setMessage('Chưa rõ trạng thái đăng nhập. Tải lại trang để kiểm tra, không tự gửi lại yêu cầu gia hạn phiên.');
      }
    }
    void load();
    return () => { active = false; };
  }, [navigate]);
  const logout = useMutation({
    mutationFn: () => postJson('/api/auth/logout', {}),
    retry: false, gcTime: 0, networkMode: 'always',
    onSuccess: () => { setEmail(null); navigate('/manager/login', { replace: true }); },
    onError: () => { setEmail(null); setMessage('Chưa xác nhận được việc đăng xuất mọi điện thoại. Hãy đăng nhập lại để kiểm tra.'); },
  });
  return <main className="mx-auto w-full max-w-md px-5 py-10">
    <p className="text-sm font-semibold text-teal-700">CapMoney</p>
    <h1 className="mt-3 text-2xl font-semibold">Khu vực quản lý</h1>
    {message && <p role="alert" className="mt-5 rounded-xl bg-white p-4">{message}</p>}
    {message && !email && <button type="button" onClick={() => navigate('/manager/login', { replace: true })}
      className="mt-4 rounded-lg bg-teal-700 p-3 font-semibold text-white">Đến trang đăng nhập</button>}
    {email ? <div className="mt-5 space-y-4"><p>Đang đăng nhập: <strong>{email}</strong></p>
      <button type="button" onClick={() => logout.mutate()} disabled={logout.isPending}
        className="rounded-lg bg-teal-700 p-3 font-semibold text-white disabled:opacity-60">Đăng xuất mọi điện thoại</button>
    </div> : !message && <p role="status" className="mt-5">Đang kiểm tra phiên đăng nhập…</p>}
  </main>;
}
