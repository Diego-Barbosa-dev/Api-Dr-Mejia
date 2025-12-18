window.ModuloUsuarios = {
  init: function(globalData) {
    console.log('[ModuloUsuarios] Inicializando...');
    const listaUsuarios = document.getElementById('listaUsuarios');
    if (!listaUsuarios) return;

    if (!Array.isArray(globalData.usuarios) || globalData.usuarios.length === 0) {
      listaUsuarios.innerHTML = '<div style="padding:20px;text-align:center;color:var(--c-text-light);">No hay usuarios registrados.</div>';
      return;
    }

    listaUsuarios.innerHTML = `
      <div style="display:grid;grid-template-columns:1fr 1fr 1fr 1fr 1fr;gap:1px;border:1px solid var(--c-border);border-radius:8px;overflow:hidden;">
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Nombre</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Email</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Rol</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Estado</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Teléfono</div>
        ${globalData.usuarios.map(u => `
          <div style="padding:12px;border-top:1px solid var(--c-border);">${u.nombre || u.fullName || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${u.email || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);"><span style="background:var(--c-primary-light);color:var(--c-primary);padding:4px 8px;border-radius:4px;font-size:12px;">${u.rol || 'ASSISTANT'}</span></div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${u.activo ? 'Activo' : 'Inactivo'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${u.telefono || u.phone || '-'}</div>
        `).join('')}
      </div>
    `;
  }
};
