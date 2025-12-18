const API_LOGIN = "/auth/login";

const els = {
  form: document.getElementById("loginForm"),
  username: document.getElementById("username"),
  password: document.getElementById("password"),
  togglePassword: document.querySelector(".toggle-password"),
  usernameHint: document.getElementById("usernameHint"),
  passwordHint: document.getElementById("passwordHint"),
  errorMsg: document.getElementById("errorMsg"),
  submitBtn: document.querySelector("button[type='submit']")
};

function setHint(el, msg) {
  if (!el) return;
  el.textContent = msg || "";
}

function showError(msg) {
  if (!els.errorMsg) return;
  els.errorMsg.textContent = msg || "Error al iniciar sesión";
  els.errorMsg.hidden = false;
}

function clearError() {
  if (!els.errorMsg) return;
  els.errorMsg.textContent = "";
  els.errorMsg.hidden = true;
}

function saveSession(token, username, roles, idHeadquarter) {
  localStorage.setItem("drm-session", JSON.stringify({ token, username, roles, idHeadquarter }));
}

// Toggle password visibility (icon button)
const setPasswordVisibility = (visible) => {
  if (!els.password || !els.togglePassword) return;
  els.password.type = visible ? "text" : "password";
  els.togglePassword.setAttribute("aria-label", visible ? "Ocultar contraseña" : "Mostrar contraseña");
  els.togglePassword.setAttribute("aria-pressed", String(visible));
  els.togglePassword.dataset.visible = visible ? "true" : "false";
};

setPasswordVisibility(false);
els.togglePassword?.addEventListener("click", () => {
  const nextState = els.password?.type === "password";
  setPasswordVisibility(nextState);
  els.password?.focus();
});

els.form?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  clearError();
  setHint(els.usernameHint, "");
  setHint(els.passwordHint, "");

  const username = els.username?.value.trim();
  const password = els.password?.value || "";

  let hasError = false;
  if (!username) {
    setHint(els.usernameHint, "Ingresa tu usuario");
    hasError = true;
  }
  if (!password) {
    setHint(els.passwordHint, "Ingresa tu contraseña");
    hasError = true;
  }
  if (hasError) return;

  const submitLabel = els.submitBtn?.textContent;
  if (els.submitBtn) {
    els.submitBtn.disabled = true;
    els.submitBtn.textContent = "Entrando…";
  }

  try {
    const res = await fetch(API_LOGIN, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (!res.ok) {
      const error = await res.json().catch(() => ({ error: "Credenciales inválidas" }));
      throw new Error(error.error || "Credenciales inválidas");
    }

    const result = await res.json();
    if (!result?.token) {
      throw new Error("Token inválido");
    }
    const roles = result.roles || [];
    
    // Obtener información adicional del usuario (sede)
    let idHeadquarter = null;
    try {
      console.log("Obteniendo info del usuario:", result.username || username);
      const infoRes = await fetch("/auth/user-info", {
        method: "POST",
        headers: { 
          "Content-Type": "application/json",
          "Authorization": `Bearer ${result.token}`
        },
        body: JSON.stringify({ username: result.username || username })
      });
      console.log("Respuesta user-info status:", infoRes.status);
      if (infoRes.ok) {
        const info = await infoRes.json();
        console.log("Info del usuario:", info);
        idHeadquarter = info.idHeadquarter || null;
        console.log("idHeadquarter obtenido:", idHeadquarter);
      } else {
        console.error("Error al obtener user-info:", await infoRes.text());
      }
    } catch (e) {
      console.warn("No se pudo obtener la sede del usuario", e);
    }
    
    console.log("Guardando sesión con idHeadquarter:", idHeadquarter);
    saveSession(result.token, result.username || username, roles, idHeadquarter);
    window.location.href = "/index.html";
  } catch (err) {
    showError(err.message || "Error al iniciar sesión");
  } finally {
    if (els.submitBtn) {
      els.submitBtn.disabled = false;
      els.submitBtn.textContent = submitLabel || "Entrar";
    }
  }
});
