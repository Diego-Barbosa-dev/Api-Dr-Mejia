// Módulo Órdenes - Lógica independiente
(function() {
  'use strict';

  // Exponer API global del módulo
  window.ModuloOrdenes = {
    init: init,
    render: renderOrdenes,
    data: null,
    helpers: {
      getOrderProviderId,
      getOrderPatientDisplay,
      getOrderPatientDocument,
      getOrderState,
      getOrderPaid,
      getOrderDateTs,
      getOrderHeadquarterId,
      getOrderConsecutivo,
      getOrderLabNumber,
      sedeNameById,
      providerNameById,
      fechaCortaTs,
      stateInfo,
      stateBadgeHtml
    },
    actions: {
      openDetails,
      payOrder,
      deleteOrder,
      markReturnRoute,
      adminSetState
    }
  };

  // Estado local del módulo
  let data = {
    ordenes: [],
    proveedores: [],
    sedes: [],
    pagos: [],
    usuarios: [],
    notificaciones: []
  };

  // ===== HELPERS - Compatibilidad de campos =====
  function getOrderProviderId(o) { 
    const id = o.idProvider ?? o.proveedorId ?? o.providerId ?? o.proveedor_id;
    return id || null;
  }
  
  function getOrderPatientDisplay(o) { 
    // Intentar obtener el nombre del paciente en orden de preferencia
    let display = (o.patientName ?? '').trim();
    if (!display) display = (o.paciente ?? '').trim();
    if (!display) display = o.documentPatient ?? '';
    if (!display) display = o.pacienteCC ?? '';
    return display || 'Sin paciente'; 
  }
  
  function getOrderPatientDocument(o) { 
    return (o.documentPatient ?? o.pacienteCC ?? '').toString().trim(); 
  }
  
  function getOrderState(o) { 
    let state = o.state ?? o.estado;
    
    // Mapear índices numéricos a estados (si vienen como números de la BD antigua)
    if (typeof state === 'number') {
      const stateMap = {
        0: 'PENDING',
        1: 'IN_PROGRESS',
        2: 'COMPLETED',
        3: 'CANCELLED'
      };
      state = stateMap[state] || 'PENDING';
    }
    
    return state || 'PENDING'; 
  }
  
  function getOrderPaid(o) { 
    return (typeof o.paid !== 'undefined' ? !!o.paid : !!o.pagada); 
  }
  
  function getOrderDateTs(o) {
    let d = o.shippingDate || o.fecha;
    if (typeof d === 'string') {
      const t = Date.parse(d);
      if (!Number.isNaN(t)) return t;
    }
    if (typeof d === 'number') return d;
    return (o.createdAt || o.idOrder || Date.now());
  }
  
  function getOrderHeadquarterId(o) { 
    return (o.idHeadquarter ?? o.headquarter ?? o.sedeId ?? null); 
  }
  
  function getOrderConsecutivo(o) { 
    return (o.consecutive ?? o.consecutivo ?? o.sequence ?? 'N/A').toString(); 
  }
  
  function getOrderLabNumber(o) { 
    return (o.number ?? o.labOrder ?? o.idOrder ?? 'N/A').toString(); 
  }

  function sedeNameById(id) {
    const s = Array.isArray(data.sedes) ? data.sedes.find(s => String(s.id) === String(id)) : null;
    return s ? (s.name || s.nombre || `Sede ${s.id}`) : (id ? `Sede ${id}` : '-');
  }

  function providerNameById(id) {
    const p = data.proveedores.find(x => String(x.id) === String(id));
    return p ? (p.name || p.nombre || p.nit || `(id ${id})`) : '(sin proveedor)';
  }

  function fechaCortaTs(ts) {
    const d = new Date(ts);
    return isNaN(d.getTime()) ? '-' : `${String(d.getDate()).padStart(2,'0')}/${String(d.getMonth()+1).padStart(2,'0')}/${d.getFullYear()}`;
  }

  function stateInfo(st) {
    // Primero convertir números a strings si es necesario
    let state = st;
    if (typeof state === 'number') {
      const stateMap = {
        0: 'PENDING',
        1: 'IN_PROGRESS',
        2: 'COMPLETED',
        3: 'CANCELLED'
      };
      state = stateMap[state] || 'PENDING';
    }
    
    const s = String(state || '').toUpperCase();
    const states = {
      'PENDING': { label: 'Pendiente', color: '#3b82f6' },
      'IN_PROGRESS': { label: 'En progreso', color: '#f59e0b' },
      'COMPLETED': { label: 'Completada', color: '#10b981' },
      'CANCELLED': { label: 'Cancelada', color: '#6b7280' },
      // Compatibilidad con estados antiguos
      'CREADA': { label: 'Creada', color: '#3b82f6' },
      'ENVIADA': { label: 'Enviada', color: '#6366f1' },
      'EN_PROGRESO': { label: 'En progreso', color: '#f59e0b' },
      'EN_RUTA': { label: 'En ruta', color: '#eab308' },
      'RECIBIDA': { label: 'Recibida', color: '#8b5cf6' },
      'COMPLETADA': { label: 'Completada', color: '#10b981' },
      'CANCELADA': { label: 'Cancelada', color: '#6b7280' }
    };
    return states[s] || { label: state || '-', color: '#9ca3af' };
  }

  function stateBadgeHtml(st) {
    // Primero convertir con getOrderState para manejar números
    const convertedState = getOrderState({ state: st });
    const info = stateInfo(convertedState);
    return `<span class="state-badge" style="--bg:${info.color};">${info.label}</span>`;
  }

  // ===== INICIALIZACIÓN =====
  function init(globalData) {
    console.log('[ModuloOrdenes] Inicializando...');
    console.log('[ModuloOrdenes] globalData:', globalData);
    data = globalData;
    ModuloOrdenes.data = data;
    console.log('[ModuloOrdenes] ordenes disponibles:', data.ordenes?.length);
    populateSelects();
    setupEventListeners();
    renderOrdenes();
  }

  // ===== POBLAR SELECTS =====
  function populateSelects() {
    // Poblar select de proveedores en el formulario
    const ordProveedor = document.getElementById('ordProveedor');
    if (ordProveedor && Array.isArray(data.proveedores)) {
      console.log('[ModuloOrdenes] Proveedores disponibles:', data.proveedores);
      ordProveedor.innerHTML = '<option value="">Seleccionar proveedor</option>';
      data.proveedores.forEach(p => {
        const option = document.createElement('option');
        option.value = p.idProvider || p.id;
        const nombre = p.name || p.nombre;
        option.textContent = nombre || `NIT: ${p.nit}`;
        console.log('[ModuloOrdenes] Proveedor:', { id: option.value, nombre, nit: p.nit, texto: option.textContent });
        ordProveedor.appendChild(option);
      });
    }

    // Poblar select de sedes en el formulario
    const ordHeadquarter = document.getElementById('ordHeadquarter');
    if (ordHeadquarter && Array.isArray(data.sedes)) {
      ordHeadquarter.innerHTML = '<option value="">Seleccionar sede</option>';
      data.sedes.forEach(s => {
        const option = document.createElement('option');
        option.value = s.idHeadquarter || s.id;
        option.textContent = s.name || s.nombre;
        ordHeadquarter.appendChild(option);
      });
    }

    // Poblar select de proveedores en filtros
    const filtroOrdenProveedor = document.getElementById('filtroOrdenProveedor');
    if (filtroOrdenProveedor && Array.isArray(data.proveedores)) {
      filtroOrdenProveedor.innerHTML = '<option value="">Todos</option>';
      data.proveedores.forEach(p => {
        const option = document.createElement('option');
        option.value = p.idProvider || p.id;
        const nombre = p.name || p.nombre;
        option.textContent = nombre || `NIT: ${p.nit}`;
        filtroOrdenProveedor.appendChild(option);
      });
    }
  }

  // ===== EVENT LISTENERS =====
  function setupEventListeners() {
    const toggleBtn = document.getElementById('toggleOrdenFilters');
    const filterContainer = document.getElementById('ordenFilters');
    const limpiarBtn = document.getElementById('limpiarFiltrosOrden');
    const formOrden = document.getElementById('formOrden');
    const cancelBtn = document.getElementById('cancelOrden');

    // Toggle filtros
    if (toggleBtn && filterContainer) {
      toggleBtn.addEventListener('click', () => {
        filterContainer.hidden = !filterContainer.hidden;
      });
    }

    // Limpiar filtros
    if (limpiarBtn) {
      limpiarBtn.addEventListener('click', () => {
        ['filtroOrdenPaciente', 'filtroOrdenProveedor', 'filtroOrdenEstado', 'filtroOrdenFecha'].forEach(id => {
          const el = document.getElementById(id);
          if (el) el.value = '';
        });
        renderOrdenes();
      });
    }

    // Listeners de filtros
    ['filtroOrdenPaciente', 'filtroOrdenProveedor', 'filtroOrdenEstado', 'filtroOrdenFecha'].forEach(id => {
      const el = document.getElementById(id);
      if (el) el.addEventListener('input', renderOrdenes);
    });

    // Tabs - Buscar directamente por data-target
    setTimeout(() => {
      const tabs = document.querySelectorAll('[data-section="ordenes"] [data-target]');
      console.log('[ModuloOrdenes] Tabs encontrados:', tabs.length);
      
      if (tabs.length === 0) {
        console.warn('[ModuloOrdenes] No se encontraron tabs');
        return;
      }
      
      const panel = document.querySelector('[data-section="ordenes"]');
      
      tabs.forEach(tab => {
        console.log('[ModuloOrdenes] Agregando listener a tab:', tab.textContent);
        tab.addEventListener('click', (e) => {
          console.log('[ModuloOrdenes] Click en tab:', e.target.textContent);
          const target = tab.getAttribute('data-target');
          console.log('[ModuloOrdenes] Target:', target);
          if (!target) return;
          
          tabs.forEach(t => t.classList.toggle('active', t === tab));
          
          // Mostrar/ocultar secciones
          const listaOrdenes = document.getElementById('listaOrdenes');
          const formOrdenDiv = document.getElementById('formOrden');
          
          console.log('[ModuloOrdenes] listaOrdenes:', !!listaOrdenes, 'formOrdenDiv:', !!formOrdenDiv);
          
          if (target === '#listaOrdenes' && listaOrdenes) {
            listaOrdenes.style.display = 'grid';
            if (formOrdenDiv) formOrdenDiv.style.display = 'none';
            console.log('[ModuloOrdenes] Mostrando lista');
          } else if (target === '#formOrden' && formOrdenDiv) {
            formOrdenDiv.style.display = 'flex';
            if (listaOrdenes) listaOrdenes.style.display = 'none';
            console.log('[ModuloOrdenes] Mostrando formulario');
          }
        });
      });
    }, 100);

    // Submit form
    if (formOrden) {
      formOrden.addEventListener('submit', async (e) => {
        e.preventDefault();
        await submitFormOrden(formOrden);
      });
    }

    // Cancelar form
    if (cancelBtn) {
      cancelBtn.addEventListener('click', (e) => {
        e.preventDefault();
        if (formOrden) {
          formOrden.style.display = 'none';
          formOrden.reset();
        }
      });
    }
  }

  // ===== RENDERIZADO =====
  function renderOrdenes() {
    const listaOrdenes = document.getElementById('listaOrdenes');
    console.log('[ModuloOrdenes] renderOrdenes - listaOrdenes encontrado:', !!listaOrdenes);
    if (!listaOrdenes) return;

    console.log('[ModuloOrdenes] data.ordenes:', data.ordenes);
    
    // Leer filtros
    const paciente = document.getElementById('filtroOrdenPaciente')?.value.trim().toLowerCase() || '';
    const provId = document.getElementById('filtroOrdenProveedor')?.value || '';
    const estado = document.getElementById('filtroOrdenEstado')?.value || '';
    const fecha = document.getElementById('filtroOrdenFecha')?.value || '';

    // Filtrar
    let ordenes = Array.isArray(data.ordenes) ? data.ordenes.filter(o => {
      const pName = getOrderPatientDisplay(o).toLowerCase();
      if (paciente && !pName.includes(paciente)) return false;
      if (provId && String(getOrderProviderId(o)) !== provId) return false;
      const stCode = String(getOrderState(o) || '').toUpperCase();
      if (estado && stCode !== estado) return false;
      if (fecha) {
        const orderDate = new Date(getOrderDateTs(o)).toISOString().slice(0, 10);
        if (orderDate !== fecha) return false;
      }
      return true;
    }) : [];

    console.log('[ModuloOrdenes] ordenes filtradas:', ordenes.length);

    // Ordenar (descendente por fecha/id)
    ordenes.sort((a, b) => {
      const cb = Number(b.createdAt ?? 0);
      const ca = Number(a.createdAt ?? 0);
      if (cb && ca && cb !== ca) return cb - ca;
      return Number((b.id ?? 0)) - Number((a.id ?? 0));
    });

    renderOrderTable(listaOrdenes, ordenes);
  }

  function renderOrderTable(container, orders) {
    console.log('[ModuloOrdenes] renderOrderTable - órdenes:', orders.length);
    if (!container) return;
    container.innerHTML = '';

    const role = localStorage.getItem('auth_role') || 'assistant';
    const isAssistant = role === 'assistant';
    const isProvider = role === 'provider';

    // Columnas visibles según rol
    const cols = [
      { header: 'Consecutivo', width: '90px', show: true },
      { header: 'Fecha envío', width: '110px', show: true },
      { header: 'Paciente / CC', width: 'minmax(200px, 2fr)', show: true },
      { header: 'Factura Dr. Mejía', width: '140px', show: true },
      { header: 'Orden laboratorio', width: '130px', show: true },
      { header: 'Factura venta lab', width: '140px', show: true },
      { header: 'Proveedor', width: '150px', show: true },
      { header: 'Sede', width: '120px', show: true },
      { header: 'Tipo lente (asesor)', width: '160px', show: !isProvider },
      { header: 'Tipo lente (admon)', width: '160px', show: (role === 'admin') },
      { header: 'Fórmula', width: '150px', show: true },
      { header: 'Estado', width: '120px', show: true },
      { header: 'Fecha de estado', width: '130px', show: (role === 'admin') },
      { header: 'Precio venta', width: '130px', show: !isProvider },
      { header: 'Precio costo', width: '130px', show: (role === 'admin') },
      { header: 'Utilidad', width: '120px', show: (!isAssistant && !isProvider) },
      { header: 'Acciones', width: '160px', show: true }
    ];

    const visibleCols = cols.filter(c => c.show);
    const gridTemplate = visibleCols.map(c => c.width).join(' ');

    // Header
    const head = document.createElement('div');
    head.className = 'table-row table-head';
    head.style.gridTemplateColumns = gridTemplate;
    head.innerHTML = visibleCols.map(c => `<div>${c.header}</div>`).join('');
    container.appendChild(head);

    if (orders.length === 0) {
      const empty = document.createElement('div');
      empty.className = 'empty';
      empty.textContent = 'No hay órdenes para mostrar.';
      container.appendChild(empty);
      return;
    }

    const fmtMoney = (val) => '$ ' + new Intl.NumberFormat('es-CO').format(val || 0);

    orders.forEach((o) => {
      const row = document.createElement('div');
      row.className = 'table-row';
      row.style.gridTemplateColumns = gridTemplate;

      const isPaid = getOrderPaid(o);
      const currState = String(getOrderState(o) || '').toUpperCase();
      const orderId = o.idOrder || o.id;
      
      // Botones de acción
      const btnsHtml = `<div class="acciones">
        <button class="btn-outline btn-sm" data-action="view" data-id="${orderId}" title="Ver detalles">
          <iconify-icon icon="ph:eye"></iconify-icon> <span class="label">Ver</span>
        </button>
        <button class="btn-outline btn-sm" data-action="pay" data-id="${orderId}" title="${isPaid ? 'Pagada' : 'Marcar pagada'}" ${isPaid ? 'disabled' : ''}>
          <iconify-icon icon="ph:check-circle"></iconify-icon> <span class="label">${isPaid ? 'Pagada' : 'Pagar'}</span>
        </button>
        <button class="btn-outline btn-sm" data-action="delete" data-id="${orderId}" title="Eliminar">
          <iconify-icon icon="ph:trash"></iconify-icon> <span class="label">Eliminar</span>
        </button>
      </div>`;

      const nombreCedula = `${getOrderPatientDisplay(o)}${getOrderPatientDocument(o) ? ' - ' + getOrderPatientDocument(o) : ''}`;
      const utilidad = (Number(o.precioVenta || 0) - Number(o.precioCosto || 0));
      const od = (o.od ?? '-').toString();
      const oi = (o.oi ?? '-').toString();
      const formula = od && od !== '-' ? `OD:${od} OI:${oi}` : '-';
      const stTs = (o.stateTimestamps && o.stateTimestamps[String(getOrderState(o)).toUpperCase()]) ? o.stateTimestamps[String(getOrderState(o)).toUpperCase()] : null;
      const tipoLenteFiltros = (o.tipoLenteFiltros ?? o.tipoLenteFiltro ?? '-').toString();
      const facturaVentaDrMejia = (o.facturaVentaDrMejia ?? '-').toString();
      const facturaVentaLaboratorio = (o.facturaVentaLaboratorio ?? '-').toString();
      const tipoLenteAdmon = (o.tipoLenteAdmon ?? '-').toString();
      const shippingDate = o.shippingDate ? fechaCortaTs(o.shippingDate) : '-';

      const values = {
        'Consecutivo': getOrderConsecutivo(o) || '-',
        'Fecha': shippingDate,
        'Paciente': nombreCedula || '-',
        'Fac. Dr Mejia': facturaVentaDrMejia,
        'Orden Lab': getOrderLabNumber(o) || '-',
        'Fac. Venta Lab': facturaVentaLaboratorio,
        'Proveedor': providerNameById(getOrderProviderId(o)) || '-',
        'Sede': sedeNameById(getOrderHeadquarterId(o)) || '-',
        'Tipo Lente (Asesor)': tipoLenteFiltros,
        'Tipo Lente (Admon)': tipoLenteAdmon,
        'Fórmula': formula || '-',
        'Estado': stateBadgeHtml(getOrderState(o)) || '-',
        'Fecha estado': stTs ? fechaCortaTs(stTs) : '-',
        'Precio Venta': fmtMoney(o.precioVenta || 0),
        'Precio Costo': fmtMoney(o.precioCosto || 0),
        'Utilidad': fmtMoney(utilidad || 0),
        'Acciones': btnsHtml
      };

      row.innerHTML = visibleCols.map(c => {
        if (c.header === 'Acciones') return values['Acciones'];
        return `<div>${values[c.header]}</div>`;
      }).join('');

      // Delegación de eventos para botones de acción
      const btns = row.querySelectorAll('[data-action]');
      btns.forEach(btn => {
        btn.addEventListener('click', handleActionClick);
      });

      container.appendChild(row);
    });
  }

  // ===== MANEJADORES DE ACCIONES =====
  function handleActionClick(e) {
    const btn = e.currentTarget;
    const action = btn.getAttribute('data-action');
    const id = Number(btn.getAttribute('data-id'));

    switch (action) {
      case 'view':
        openDetails(id);
        break;
      case 'pay':
        payOrder(id);
        break;
      case 'delete':
        deleteOrder(id);
        break;
    }
  }

  function openDetails(orderId) {
    const orden = data.ordenes.find(o => o.id === orderId);
    if (!orden) {
      alert('Orden no encontrada');
      return;
    }
    console.log('📋 Ver detalles de orden:', orden);
    // TODO: Implementar modal de detalles
  }

  async function payOrder(orderId) {
    const orden = data.ordenes.find(o => o.id === orderId);
    if (!orden) {
      alert('Orden no encontrada');
      return;
    }

    if (confirm(`¿Marcar orden ${getOrderConsecutivo(orden) || orderId} como pagada?`)) {
      try {
        await window.Api.patch(`/api/orders/${orderId}`, { paid: true });
        orden.paid = true;
        renderOrdenes();
        console.log('✅ Orden marcada como pagada');
      } catch (err) {
        console.error('❌ Error al marcar pagada:', err);
        alert('Error: ' + err.message);
      }
    }
  }

  async function deleteOrder(orderId) {
    const orden = data.ordenes.find(o => o.id === orderId);
    if (!orden) {
      alert('Orden no encontrada');
      return;
    }

    if (confirm(`¿Eliminar orden ${getOrderConsecutivo(orden) || orderId}?`)) {
      try {
        await window.Api.delete(`/api/orders/${orderId}`);
        data.ordenes = data.ordenes.filter(o => o.id !== orderId);
        renderOrdenes();
        console.log('✅ Orden eliminada');
      } catch (err) {
        console.error('❌ Error al eliminar:', err);
        alert('Error: ' + err.message);
      }
    }
  }

  function markReturnRoute(orderId) {
    const orden = data.ordenes.find(o => o.id === orderId);
    if (!orden) {
      alert('Orden no encontrada');
      return;
    }
    console.log('🔄 Marcar retorno en ruta para:', orden);
    // TODO: Implementar lógica de retorno
  }

  function adminSetState(orderId, newState) {
    const orden = data.ordenes.find(o => o.id === orderId);
    if (!orden) {
      alert('Orden no encontrada');
      return;
    }
    console.log(`📊 Cambiar estado de orden ${orderId} a ${newState}`);
    // TODO: Implementar cambio de estado
  }

  // ===== FORMULARIO =====
  async function submitFormOrden(form) {
    const ordPaciente = document.getElementById('ordPaciente');
    const ordProveedor = document.getElementById('ordProveedor');
    const ordHeadquarter = document.getElementById('ordHeadquarter');
    const ordNumero = document.getElementById('ordNumero');
    const ordPacienteCC = document.getElementById('ordPacienteCC');

    if (!ordPaciente || !ordProveedor || !ordPaciente.value.trim() || !ordProveedor.value) {
      alert('Paciente y proveedor son obligatorios');
      return;
    }

    if (!ordPacienteCC || !ordPacienteCC.value.trim()) {
      alert('La cédula del paciente es obligatoria');
      return;
    }

    try {
      // Paso 1: Verificar si el paciente ya existe
      const documentPatient = ordPacienteCC.value.trim();
      const patientName = ordPaciente.value.trim();
      
      let patientExists = false;
      try {
        const patients = await window.Api.get('/api/patients');
        patientExists = patients.some(p => p.document === documentPatient);
      } catch (err) {
        console.error('Error verificando pacientes:', err);
      }
      
      // Solo crear el paciente si no existe
      if (!patientExists) {
        try {
          await window.Api.post('/api/patients', {
            document: documentPatient,
            name: patientName
          });
          console.log('✅ Paciente creado');
        } catch (patientErr) {
          console.error('❌ Error creando paciente:', patientErr);
          alert('Error al crear paciente: ' + patientErr.message);
          return;
        }
      } else {
        console.log('ℹ️ Paciente ya existe, continuando...');
      }

      // Paso 2: Crear la orden
      const payload = {
        patientName: patientName,
        number: ordNumero?.value.trim() || 'N/A',
        idProvider: Number(ordProveedor.value),
        documentPatient: documentPatient,
        state: 'PENDING',
        paid: false
      };

      // Agregar sede si fue seleccionada
      if (ordHeadquarter && ordHeadquarter.value) {
        payload.idHeadquarter = Number(ordHeadquarter.value);
      } else {
        // Usar la primera sede disponible si no se seleccionó ninguna
        if (data.sedes && data.sedes.length > 0) {
          payload.idHeadquarter = data.sedes[0].idHeadquarter || data.sedes[0].id;
        }
      }

      await window.Api.post('/api/orders', payload);
      
      // Recargar las órdenes desde el servidor
      try {
        const ordenes = await window.Api.get('/api/orders');
        data.ordenes = Array.isArray(ordenes) ? ordenes : [];
        console.log('✅ Órdenes recargadas:', data.ordenes.length);
      } catch (loadErr) {
        console.error('Error recargando órdenes:', loadErr);
      }
      
      form.reset();
      form.style.display = 'none';
      renderOrdenes();
      console.log('✅ Orden creada');
      alert('Orden creada exitosamente');
    } catch (err) {
      console.error('❌ Error creando orden:', err);
      alert('Error: ' + err.message);
    }
  }

})();
