window.ModuloNotificaciones = {
  init: function(globalData) {
    console.log('[ModuloNotificaciones] Inicializando...');
    const listaNotificaciones = document.getElementById('listaNotificaciones');
    if (!listaNotificaciones) return;

    // Crear notificaciones de órdenes pendientes
    const notificaciones = [
      ...((globalData.ordenes || []).filter(o => ['CREADA', 'EN_PROGRESO'].includes(o.estado)).map(o => ({
        tipo: 'orden',
        mensaje: `Orden ${o.consecutivo || o.id} en estado ${o.estado}`,
        fecha: o.fecha,
        icon: 'ph:files'
      }))),
      ...((globalData.pagos || []).filter(p => ['PENDIENTE', 'PENDIENTE_CONFIRMACION'].includes(p.estado)).map(p => ({
        tipo: 'pago',
        mensaje: `Pago pendiente de $ ${new Intl.NumberFormat('es-CO').format(p.monto || 0)}`,
        fecha: p.fecha,
        icon: 'ph:credit-card'
      })))
    ].slice(0, 10);

    if (notificaciones.length === 0) {
      listaNotificaciones.innerHTML = '<div style="padding:20px;text-align:center;color:var(--c-text-light);">No hay notificaciones.</div>';
      return;
    }

    listaNotificaciones.innerHTML = notificaciones.map(n => `
      <div style="background:var(--c-card);border:1px solid var(--c-border);border-radius:8px;padding:16px;margin-bottom:12px;display:flex;gap:12px;">
        <div style="font-size:24px;display:flex;align-items:center;"><iconify-icon icon="${n.icon}"></iconify-icon></div>
        <div style="flex:1;">
          <div style="font-weight:600;">${n.mensaje}</div>
          <div style="font-size:12px;color:var(--c-text-light);margin-top:4px;">${n.fecha ? new Date(n.fecha).toLocaleDateString('es-CO') : 'Hoy'}</div>
        </div>
      </div>
    `).join('');
  }
};
