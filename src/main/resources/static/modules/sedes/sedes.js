window.ModuloSedes = {
  init: function(globalData) {
    console.log('[ModuloSedes] Inicializando...');
    const listaSedes = document.getElementById('listaSedes');
    if (!listaSedes) return;

    if (!Array.isArray(globalData.sedes) || globalData.sedes.length === 0) {
      listaSedes.innerHTML = '<div style="padding:20px;text-align:center;color:var(--c-text-light);">No hay sedes registradas.</div>';
      return;
    }

    listaSedes.innerHTML = `
      <div style="display:grid;grid-template-columns:1fr 1fr 1fr 1fr;gap:1px;border:1px solid var(--c-border);border-radius:8px;overflow:hidden;">
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Nombre</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Ciudad</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Dirección</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Teléfono</div>
        ${globalData.sedes.map(s => `
          <div style="padding:12px;border-top:1px solid var(--c-border);">${s.nombre || s.name || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${s.ciudad || s.city || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${s.direccion || s.address || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${s.telefono || s.phone || '-'}</div>
        `).join('')}
      </div>
    `;
  }
};
