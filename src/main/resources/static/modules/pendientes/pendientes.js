window.ModuloPendientes = {
  init: function(globalData) {
    console.log('[ModuloPendientes] Inicializando...');
    const listaPendientes = document.getElementById('listaPendientes');
    if (!listaPendientes) return;

    // Filtrar órdenes pendientes
    const ordenesPendientes = Array.isArray(globalData.ordenes) 
      ? globalData.ordenes.filter(o => ['CREADA', 'EN_PROGRESO', 'EN_RUTA'].includes(o.estado))
      : [];

    if (ordenesPendientes.length === 0) {
      listaPendientes.innerHTML = '<div style="padding:20px;text-align:center;color:var(--c-text-light);">No hay órdenes pendientes.</div>';
      return;
    }

    // Crear tabla
    listaPendientes.innerHTML = `
      <div style="display:grid;grid-template-columns:1fr 1fr 1fr 1fr 1fr;gap:1px;border:1px solid var(--c-border);border-radius:8px;overflow:hidden;">
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Consecutivo</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Paciente</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Estado</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Fecha</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Proveedor</div>
        ${ordenesPendientes.map(o => `
          <div style="padding:12px;border-top:1px solid var(--c-border);">${o.consecutivo || o.id || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${o.paciente || o.patientName || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);"><span style="background:#fff3cd;color:#856404;padding:4px 8px;border-radius:4px;font-size:12px;">${o.estado || '-'}</span></div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${o.fecha ? new Date(o.fecha).toLocaleDateString('es-CO') : '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${o.proveedor || 'N/A'}</div>
        `).join('')}
      </div>
    `;
  }
};

