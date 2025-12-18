window.ModuloProveedores = {
  globalData: null,

  init: function(globalData) {
    console.log('[ModuloProveedores] Inicializando...');
    this.globalData = globalData;
    this.renderProveedores();
    setupEventListeners();
  },

  renderProveedores: function() {
    const listaProveedores = document.getElementById('listaProveedores');
    if (!listaProveedores) return;

    if (!Array.isArray(this.globalData.proveedores) || this.globalData.proveedores.length === 0) {
      listaProveedores.innerHTML = '<div style="padding:20px;text-align:center;color:var(--c-text-light);">No hay proveedores registrados.</div>';
      return;
    }

    listaProveedores.innerHTML = `
      <div style="display:grid;grid-template-columns:1fr 1fr 1fr 1fr 1fr 1fr;gap:1px;border:1px solid var(--c-border);border-radius:8px;overflow:hidden;">
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Nombre</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">NIT</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Teléfono</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Email</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Dirección</div>
        <div style="background:var(--c-bg-secondary);padding:12px;font-weight:600;font-size:13px;">Ciudad</div>
        ${this.globalData.proveedores.map(p => `
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.name || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.nit || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.phone || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.email || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.address || '-'}</div>
          <div style="padding:12px;border-top:1px solid var(--c-border);">${p.city || '-'}</div>
        `).join('')}
      </div>
    `;
  },

  loadProveedores: function() {
    fetch('/api/providers', {
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    })
    .then(response => response.json())
    .then(data => {
      this.globalData.proveedores = data;
      this.renderProveedores();
    })
    .catch(error => console.error('[ModuloProveedores] Error cargando proveedores:', error));
  }
};

// ===== EVENT LISTENERS =====
function setupEventListeners() {
  // Tabs - Buscar directamente por data-target
  setTimeout(() => {
    const tabs = document.querySelectorAll('[data-section="proveedores"] [data-target]');
    console.log('[ModuloProveedores] Tabs encontrados:', tabs.length);
    
    if (tabs.length === 0) {
      console.warn('[ModuloProveedores] No se encontraron tabs');
      return;
    }
    
    const panel = document.querySelector('[data-section="proveedores"]');
    
    tabs.forEach(tab => {
      console.log('[ModuloProveedores] Agregando listener a tab:', tab.textContent);
      tab.addEventListener('click', (e) => {
        console.log('[ModuloProveedores] Click en tab:', e.target.textContent);
        const target = tab.getAttribute('data-target');
        console.log('[ModuloProveedores] Target:', target);
        if (!target) return;
        
        tabs.forEach(t => t.classList.toggle('active', t === tab));
        
        // Mostrar/ocultar secciones
        const listaProveedores = document.getElementById('listaProveedores');
        const formProveedor = document.getElementById('formProveedor');
        
        console.log('[ModuloProveedores] listaProveedores:', !!listaProveedores, 'formProveedor:', !!formProveedor);
        
        if (target === '#listaProveedores' && listaProveedores) {
          listaProveedores.style.display = 'grid';
          if (formProveedor) formProveedor.style.display = 'none';
          console.log('[ModuloProveedores] Mostrando lista');
        } else if (target === '#formProveedor' && formProveedor) {
          formProveedor.style.display = 'flex';
          if (listaProveedores) listaProveedores.style.display = 'none';
          console.log('[ModuloProveedores] Mostrando formulario');
        }
      });
    });
  }, 100);

  // Form submit
  const formProveedor = document.getElementById('formProveedor');
  if (formProveedor) {
    formProveedor.addEventListener('submit', function(e) {
      e.preventDefault();
      submitFormProveedor();
    });
  }

  // Cancel button
  const cancelBtn = document.getElementById('cancelProveedor');
  if (cancelBtn) {
    cancelBtn.addEventListener('click', function() {
      formProveedor.style.display = 'none';
      document.getElementById('listaProveedores').style.display = 'grid';
      document.querySelectorAll('[data-section="proveedores"] .tab').forEach(t => {
        t.classList.remove('active');
        if (t.getAttribute('data-target') === '#listaProveedores') {
          t.classList.add('active');
        }
      });
    });
  }
}

function submitFormProveedor() {
  const nombre = document.getElementById('provNombre').value.trim();
  const nit = document.getElementById('provNit').value.trim();
  const telefono = document.getElementById('provTelefono').value.trim();
  const email = document.getElementById('provEmail').value.trim();
  const direccion = document.getElementById('provDireccion').value.trim();
  const ciudad = document.getElementById('provCiudad').value.trim();

  if (!nombre || !nit || !email || !direccion) {
    alert('Por favor completa los campos requeridos (Nombre, NIT, Email y Dirección)');
    return;
  }

  const proveedor = {
    name: nombre,
    nit: nit,
    email: email,
    address: direccion,
    phone: telefono,
    city: ciudad
  };

  console.log('[ModuloProveedores] Guardando proveedor:', proveedor);

  fetch('/api/providers', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    },
    body: JSON.stringify(proveedor)
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Error al guardar proveedor');
    }
    return response.json();
  })
  .then(data => {
    console.log('[ModuloProveedores] Proveedor guardado:', data);
    alert('Proveedor guardado exitosamente');
    
    // Reset form
    document.getElementById('formProveedor').reset();
    document.getElementById('formProveedor').style.display = 'none';
    document.getElementById('listaProveedores').style.display = 'grid';
    
    // Update active tab
    document.querySelectorAll('[data-section="proveedores"] .tab').forEach(t => {
      t.classList.remove('active');
      if (t.getAttribute('data-target') === '#listaProveedores') {
        t.classList.add('active');
      }
    });
    
    // Reload list
    window.ModuloProveedores.loadProveedores();
  })
  .catch(error => {
    console.error('[ModuloProveedores] Error:', error);
    alert('Error al guardar proveedor: ' + error.message);
  });
}
