window.ModuloReportes = {
  init: function(globalData) {
    console.log('[ModuloReportes] Inicializando...');
    const listaReportes = document.getElementById('listaReportes');
    if (!listaReportes) return;

    const totalOrdenes = (globalData.ordenes || []).length;
    const totalPagos = (globalData.pagos || []).reduce((sum, p) => sum + (p.monto || 0), 0);
    const totalProveedores = (globalData.proveedores || []).length;

    listaReportes.innerHTML = `
      <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(250px,1fr));gap:16px;">
        <div style="background:var(--c-card);border:1px solid var(--c-border);border-radius:8px;padding:20px;">
          <div style="color:var(--c-text-light);font-size:13px;margin-bottom:8px;">Total de órdenes</div>
          <div style="font-size:32px;font-weight:700;">${totalOrdenes}</div>
        </div>
        <div style="background:var(--c-card);border:1px solid var(--c-border);border-radius:8px;padding:20px;">
          <div style="color:var(--c-text-light);font-size:13px;margin-bottom:8px;">Total de pagos</div>
          <div style="font-size:32px;font-weight:700;">$ ${new Intl.NumberFormat('es-CO').format(totalPagos)}</div>
        </div>
        <div style="background:var(--c-card);border:1px solid var(--c-border);border-radius:8px;padding:20px;">
          <div style="color:var(--c-text-light);font-size:13px;margin-bottom:8px;">Proveedores activos</div>
          <div style="font-size:32px;font-weight:700;">${totalProveedores}</div>
        </div>
      </div>
    `;
  }
};
