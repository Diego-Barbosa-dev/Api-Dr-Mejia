window.ModuloPagos = {
  init: function(globalData) {
    console.log('[ModuloPagos] Inicializando...');
    const listaPagos = document.getElementById('listaPagos');
    if (!listaPagos) return;

    if (!Array.isArray(globalData.pagos) || globalData.pagos.length === 0) {
      listaPagos.innerHTML = '<div style="padding:20px;text-align:center;color:var(--c-text-light);">No hay pagos registrados.</div>';
      return;
    }

    // Crear tabla
    listaPagos.innerHTML = `
      <div style="display:grid;grid-template-columns:1fr 1fr 1fr 1fr 1fr;gap:1px;border:1px solid var(--c-border);border-radius:8px;overflow:hidden;">
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">ID</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Monto</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Fecha</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Estado</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Descripción</div>
        ${globalData.pagos.map(p => `
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.id || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">$ ${new Intl.NumberFormat('es-CO').format(p.monto || 0)}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.fecha ? new Date(p.fecha).toLocaleDateString('es-CO') : '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);"><span style="background:var(--c-primary-light);color:var(--c-primary);padding:4px 8px;border-radius:4px;font-size:12px;">${p.estado || 'PENDIENTE'}</span></div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.descripcion || '-'}</div>
        `).join('')}
      </div>
    `;
  }
};

