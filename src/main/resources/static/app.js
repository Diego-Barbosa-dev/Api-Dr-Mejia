const state = {
  token: null,
  roles: [],
  username: null,
  idHeadquarter: null,
  orders: [],
  headquarters: [],
  patients: [],
  users: [],
  rolesData: [],
  providers: [],
  payments: []
};

const flags = {
  admin: false,
  assistant: false,
  provider: false
};

const els = {
  appShell: document.getElementById("app-shell"),
  navMenu: document.getElementById("nav-menu"),
  sectionTitle: document.getElementById("section-title"),
  statusChip: document.getElementById("status-chip"),
  userName: document.getElementById("user-name"),
  userRoles: document.getElementById("user-roles"),
  logoutBtn: document.getElementById("logout-btn"),
  toast: document.getElementById("toast"),
  // filters
  filterState: document.getElementById("filter-state"),
  filterSearch: document.getElementById("filter-search"),
  refreshOrders: document.getElementById("refresh-orders"),
  exportOrders: document.getElementById("export-orders"),
  // sections
  sections: {
    dashboard: document.getElementById("section-dashboard"),
    orders: document.getElementById("section-orders"),
    ordersByHq: document.getElementById("section-orders-by-hq"),
    users: document.getElementById("section-users"),
    headquarters: document.getElementById("section-headquarters"),
    providers: document.getElementById("section-providers"),
    payments: document.getElementById("section-payments"),
    reports: document.getElementById("section-reports")
  },
  // tables/panels
  ordersTable: document.getElementById("orders-table"),
  orderForm: document.getElementById("order-form"),
  orderReset: document.getElementById("order-reset"),
  orderCancel: document.getElementById("order-cancel"),
  orderSubmit: document.getElementById("order-submit"),
  orderFormPanel: document.getElementById("order-form-panel"),
  orderHqSelect: document.getElementById("order-hq-select"),
  orderProviderSelect: document.getElementById("order-provider-select"),
  dashboardCards: document.getElementById("dashboard-cards"),
  metricOrders: document.getElementById("metric-orders"),
  metricOrdersSub: document.getElementById("metric-orders-sub"),
  metricPending: document.getElementById("metric-pending"),
  metricProgress: document.getElementById("metric-progress"),
  metricCompleted: document.getElementById("metric-completed"),
  metricToday: document.getElementById("metric-today"),
  metricLate: document.getElementById("metric-late"),
  metricProviders: document.getElementById("metric-providers"),
  upcomingList: document.getElementById("upcoming-list"),
  stateBars: document.getElementById("state-bars"),
  headquarterCards: document.getElementById("headquarter-cards"),
  reportHqBars: document.getElementById("report-hq-bars"),
  usersTable: document.getElementById("users-table"),
  userForm: document.getElementById("user-form"),
  userCancel: document.getElementById("user-cancel"),
  userFormPanel: document.getElementById("user-form-panel"),
  userRoleSelect: document.getElementById("user-role-select"),
  userHqSelect: document.getElementById("user-hq-select"),
  rolesTable: document.getElementById("roles-table"),
  roleForm: document.getElementById("role-form"),
  usersListView: document.getElementById("users-list-view"),
  rolesView: document.getElementById("roles-view"),
  btnUsersList: document.getElementById("btn-users-list"),
  btnUsersCreate: document.getElementById("btn-users-create"),
  btnUsersRoles: document.getElementById("btn-users-roles"),
  refreshUsers: document.getElementById("refresh-users"),
  refreshRoles: document.getElementById("refresh-roles"),
  headquartersTable: document.getElementById("headquarters-table"),
  hqForm: document.getElementById("hq-form"),
  hqFormPanel: document.getElementById("hq-form-panel"),
  hqListView: document.getElementById("hq-list-view"),
  hqCancel: document.getElementById("hq-cancel"),
  hqFilterId: document.getElementById("hq-filter-id"),
  hqSort: document.getElementById("hq-sort"),
  btnHqList: document.getElementById("btn-hq-list"),
  btnHqCreate: document.getElementById("btn-hq-create"),
  refreshHeadquarters: document.getElementById("refresh-headquarters"),
  providersTable: document.getElementById("providers-table"),
  providerForm: document.getElementById("provider-form"),
  providerFormPanel: document.getElementById("provider-form-panel"),
  providersListView: document.getElementById("providers-list-view"),
  providerCancel: document.getElementById("provider-cancel"),
  btnProvidersList: document.getElementById("btn-providers-list"),
  btnProvidersCreate: document.getElementById("btn-providers-create"),
  refreshProviders: document.getElementById("refresh-providers"),
  // Payments
  paymentsTable: document.getElementById("payments-table"),
  paymentsListView: document.getElementById("payments-list-view"),
  paymentForm: document.getElementById("payment-form"),
  paymentCancel: document.getElementById("payment-cancel"),
  refreshPayments: document.getElementById("refresh-payments"),
  exportPayments: document.getElementById("export-payments"),
  paymentOrderSelect: document.getElementById("payment-order-select"),
  paymentFilterOrder: document.getElementById("payment-filter-order"),
  paymentFormPanel: document.getElementById("payment-form-panel"),
  btnPaymentsList: document.getElementById("btn-payments-list"),
  btnPaymentsCreate: document.getElementById("btn-payments-create"),
  // Orders view toggle
  btnOrdersList: document.getElementById("btn-orders-list"),
  btnOrdersCreate: document.getElementById("btn-orders-create"),
  ordersListView: document.getElementById("orders-list-view"),
  // Eyes modal
  eyesModal: document.getElementById("eyes-modal"),
  closeEyesModal: document.getElementById("close-eyes-modal"),
  eyesModalBody: document.getElementById("eyes-modal-body"),
  // State modal
  stateModal: document.getElementById("state-modal"),
  closeStateModal: document.getElementById("close-state-modal"),
  stateModalBody: document.getElementById("state-modal-body"),
  // Received modal
  receivedModal: document.getElementById("received-modal"),
  closeReceivedModal: document.getElementById("close-received-modal"),
  receivedForm: document.getElementById("received-form"),
  receivedBy: document.getElementById("received-by"),
  receivedCancel: document.getElementById("received-cancel"),
  // Cost modal
  costModal: document.getElementById("cost-modal"),
  closeCostModal: document.getElementById("close-cost-modal"),
  costForm: document.getElementById("cost-form"),
  costCurrent: document.getElementById("cost-current"),
  costNew: document.getElementById("cost-new"),
  labVoucher: document.getElementById("lab-voucher"),
  costCancel: document.getElementById("cost-cancel")
};

const API = {
  login: "/auth/login",
  orders: "/api/orders",
  headquarters: "/api/headquarters",
  users: "/api/users",
  roles: "/api/roles",
  providers: "/api/providers",
  patients: "/api/patients",
  payments: "/api/payments",
  eyes: "/api/eyes"
};

async function apiFetch(path, options = {}) {
  const headers = options.headers ? { ...options.headers } : {};
  if (state.token) headers["Authorization"] = `Bearer ${state.token}`;
  if (options.body && !headers["Content-Type"]) headers["Content-Type"] = "application/json";

  const res = await fetch(path, { ...options, headers });
  if (res.status === 204) return null;
  const isJson = res.headers.get("content-type")?.includes("application/json");
  if (!res.ok) {
    const message = isJson ? (await res.json()).error || res.statusText : res.statusText;
    throw new Error(message);
  }
  return isJson ? res.json() : res.text();
}

function saveSession() {
  localStorage.setItem("drm-session", JSON.stringify({
    token: state.token,
    roles: state.roles,
    username: state.username,
    idHeadquarter: state.idHeadquarter
  }));
}

async function fetchUserHeadquarter() {
  try {
    console.log("Obteniendo userHeadquarter del servidor...");
    console.log("Username actual:", state.username);
    const response = await apiFetch("/auth/user-info", {
      method: "POST",
      body: JSON.stringify({ username: state.username })
    });
    
    state.idHeadquarter = response.idHeadquarter || null;
    console.log("idHeadquarter obtenido:", state.idHeadquarter);
    // Actualizar sesión en localStorage
    saveSession();
  } catch (error) {
    console.error("Error en fetchUserHeadquarter:", error);
  }
}

function restoreSession() {
  try {
    const raw = localStorage.getItem("drm-session");
    if (!raw) {
      window.location.href = "/login.html";
      return;
    }
    const session = JSON.parse(raw);
    if (session?.token) {
      state.token = session.token;
      state.roles = session.roles || [];
      state.username = session.username || "";
      state.idHeadquarter = session.idHeadquarter || null;
      console.log("Sesión cargada - idHeadquarter:", state.idHeadquarter);
      computeFlags();
      
      // Si no hay idHeadquarter pero el usuario no es admin, obtenerlo del servidor
      if (!flags.admin && !state.idHeadquarter && state.username) {
        fetchUserHeadquarter().then(() => enterApp());
      } else {
        enterApp();
      }
    } else {
      window.location.href = "/login.html";
    }
  } catch (err) {
    console.warn("No se pudo restaurar sesión", err);
    window.location.href = "/login.html";
  }
}

function showToast(message) {
  els.toast.textContent = message;
  els.toast.classList.remove("hidden");
  requestAnimationFrame(() => els.toast.classList.add("show"));
  setTimeout(() => {
    els.toast.classList.remove("show");
    setTimeout(() => els.toast.classList.add("hidden"), 200);
  }, 2400);
}

function setStatus(text, ok = true) {
  els.statusChip.textContent = text;
  if (ok) {
    els.statusChip.style.background = "#f0fdf4";
    els.statusChip.style.color = "#22c55e";
    els.statusChip.style.borderColor = "#22c55e";
  } else {
    els.statusChip.style.background = "#fef2f2";
    els.statusChip.style.color = "#ef4444";
    els.statusChip.style.borderColor = "#ef4444";
  }
}

function resolveSectionName(name) {
  if (name === "orders-by-hq") return "ordersByHq";
  return name;
}

function computeFlags() {
  const roles = state.roles || [];
  flags.admin = roles.some(r => /ADMIN/i.test(r));
  flags.assistant = roles.some(r => /(ASSISTANT|ASISTENTE)/i.test(r));
  flags.provider = roles.some(r => /(PROVIDER|PROVEEDOR)/i.test(r));
}

function applyRoleVisibility() {
  els.orderFormPanel?.classList.toggle("hidden", !(flags.admin || flags.assistant));
  els.navMenu.querySelectorAll(".menu-item").forEach(btn => {
    const allowed = (btn.dataset.visibleFor || "").split(",").map(r => r.trim()).filter(Boolean);
    const canSee = (
      (flags.admin && allowed.includes("admin")) ||
      (flags.assistant && allowed.includes("assistant")) ||
      (flags.provider && allowed.includes("provider"))
    );
    btn.style.display = canSee ? "block" : "none";
  });

  if (flags.provider) {
    document.querySelectorAll(".admin-only").forEach(el => el.classList.add("hidden"));
  }
  if (flags.provider || (!flags.admin && !flags.assistant)) {
    document.querySelectorAll(".admin-assistant").forEach(el => el.classList.add("hidden"));
  }
  if (!flags.admin) {
    document.querySelectorAll(".admin-only").forEach(el => el.classList.add("hidden"));
  }
}

function switchSection(name) {
  const target = resolveSectionName(name);
  Object.entries(els.sections).forEach(([key, node]) => {
    if (node) node.classList.toggle("hidden", key !== target);
  });
  const activeBtn = [...els.navMenu.querySelectorAll(".menu-item")]
    .find(btn => btn.dataset.section === name);
  els.navMenu.querySelectorAll(".menu-item").forEach(btn => btn.classList.remove("active"));
  if (activeBtn) activeBtn.classList.add("active");
  const titles = {
    dashboard: "Dashboard",
    orders: "Órdenes",
    "orders-by-hq": "Órdenes por sede",
    ordersByHq: "Órdenes por sede",
    users: "Usuarios",
    headquarters: "Sedes",
    reports: "Reportes"
  };
  els.sectionTitle.textContent = titles[name] || titles[target] || "";
}

async function bootstrapData() {
  const fetches = [loadOrders(), loadHeadquarters(), loadPatients()];
  if (flags.admin || flags.assistant) {
    fetches.push(loadProviders(), loadPayments());
  }
  if (flags.admin) {
    fetches.push(loadUsers(), loadRoles());
  }
  await Promise.all(fetches);
  updateDashboard();
  updateReports();
  updateHeadquarterBlocks();
}

function enterApp() {
  els.appShell.classList.remove("hidden");
  els.userName.textContent = state.username || "Usuario";
  els.userRoles.textContent = state.roles.join(", ");
  computeFlags();
  applyRoleVisibility();
  setStatus(flags.admin ? "Admin" : flags.assistant ? "Asistente" : "Proveedor");
  saveSession();
  const firstVisible = [...els.navMenu.querySelectorAll(".menu-item")].find(btn => btn.style.display !== "none");
  switchSection(firstVisible?.dataset.section || "dashboard");
  bootstrapData();
}

els.navMenu.addEventListener("click", (ev) => {
  const btn = ev.target.closest(".menu-item");
  if (!btn) return;
  const section = resolveSectionName(btn.dataset.section);
  switchSection(btn.dataset.section);
  if (section === "dashboard") updateDashboard();
  if (section === "orders") {
    setOrdersView("list");
  }
  if (section === "ordersByHq") updateHeadquarterBlocks();
  if (section === "headquarters") setHqView("list");
  if (section === "payments") setPaymentsView("list");
  if (section === "providers") setProvidersView("list");
  if (section === "reports") updateReports();
  if (section === "users") setUsersView("list");
});

// Filters
[els.filterState, els.filterSearch].forEach(ctrl => ctrl?.addEventListener("input", renderOrders));
els.refreshOrders?.addEventListener("click", loadOrders);

// Orders form
els.orderForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  
  // Prevenir múltiples envíos
  if (els.orderSubmit.disabled) return;
  els.orderSubmit.disabled = true;
  const originalText = els.orderSubmit.textContent;
  els.orderSubmit.textContent = "Guardando...";
  
  const data = new FormData(ev.target);
  const editingId = els.orderForm.dataset.editingId;
  
  // Validación manual de ojos requeridos
  const requiredEyeFields = [
    "leftEsf", "leftCil", "leftAxis", "leftAddition", "leftDp", "leftHigh", "leftAvl", "leftAvp",
    "rightEsf", "rightCil", "rightAxis", "rightAddition", "rightDp", "rightHigh", "rightAvl", "rightAvp"
  ];
  const missing = requiredEyeFields.filter(f => !(data.get(f) || "").toString().trim());
  if (missing.length) {
    showToast("Completa todos los campos de los ojos");
    els.orderSubmit.disabled = false;
    els.orderSubmit.textContent = originalText;
    return;
  }

  const patientBody = {
    document: data.get("documentPatient")?.trim(),
    name: data.get("patientName")?.trim(),
    email: data.get("patientEmail")?.trim(),
    address: data.get("patientAddress")?.trim(),
    notes: (data.get("patientNotes") || "").trim()
  };

  const orderBody = {
    number: data.get("number"),
    documentPatient: data.get("documentPatient"),
    idHeadquarter: numberOrNull(data.get("idHeadquarter")),
    idProvider: numberOrNull(data.get("idProvider")),
    salePrice: numberOrNull(data.get("salePrice")),
    costPrice: numberOrNull(data.get("costPrice")),
    frameType: data.get("frameType"),
    lensType: data.get("lensType"),
    shippingDate: data.get("shippingDate"),
    deliveryDate: data.get("deliveryDate") || null,
    daysPassed: null, // El backend calcula esto automáticamente
    state: data.get("state")
  };
  
  try {
    // Siempre intentar crear el paciente si no existe
    if (!editingId) {
      try {
        await apiFetch(API.patients, { method: "POST", body: JSON.stringify(patientBody) });
        // Añadir al estado local si se creó exitosamente
        state.patients.push({ ...patientBody });
      } catch (patientErr) {
        // Si falla porque ya existe, continuar (es esperado)
        // Si falla por otro motivo, también continuamos porque el backend validará
        console.log("Paciente ya existe o será validado por el backend:", patientErr.message);
      }
    }

    if (editingId) {
      // Actualizar orden existente
      await apiFetch(`${API.orders}/${editingId}`, { method: "PUT", body: JSON.stringify(orderBody) });

      const leftEyeBody = {
        idEye: numberOrNull(els.orderForm.dataset.leftEyeId),
        order: numberOrNull(editingId),
        type: "LEFT",
        esf: data.get("leftEsf"),
        cil: data.get("leftCil"),
        axis: data.get("leftAxis"),
        addition: data.get("leftAddition"),
        dp: data.get("leftDp"),
        high: data.get("leftHigh"),
        avl: data.get("leftAvl"),
        avp: data.get("leftAvp")
      };
      if (leftEyeBody.idEye) {
        await apiFetch(`${API.eyes}/${leftEyeBody.idEye}`, { method: "PUT", body: JSON.stringify(leftEyeBody) });
      } else {
        await apiFetch(API.eyes, { method: "POST", body: JSON.stringify(leftEyeBody) });
      }

      const rightEyeBody = {
        idEye: numberOrNull(els.orderForm.dataset.rightEyeId),
        order: numberOrNull(editingId),
        type: "RIGHT",
        esf: data.get("rightEsf"),
        cil: data.get("rightCil"),
        axis: data.get("rightAxis"),
        addition: data.get("rightAddition"),
        dp: data.get("rightDp"),
        high: data.get("rightHigh"),
        avl: data.get("rightAvl"),
        avp: data.get("rightAvp")
      };
      if (rightEyeBody.idEye) {
        await apiFetch(`${API.eyes}/${rightEyeBody.idEye}`, { method: "PUT", body: JSON.stringify(rightEyeBody) });
      } else {
        await apiFetch(API.eyes, { method: "POST", body: JSON.stringify(rightEyeBody) });
      }

      showToast("Orden actualizada");
    } else {
      // Crear nueva orden
      const createdOrder = await apiFetch(API.orders, { method: "POST", body: JSON.stringify(orderBody) });
      let orderId = createdOrder?.idOrder || createdOrder?.id;

      // Si no llegó el ID, buscar en el estado tras recargar
      if (!orderId) {
        await loadOrders();
        const found = (state.orders || []).find(o =>
          o.number === orderBody.number && o.documentPatient === orderBody.documentPatient
        );
        orderId = found?.idOrder || found?.id;
      }

      if (!orderId) {
        throw new Error("No se pudo obtener ID de la orden");
      }

      const leftEyeBody = {
        order: orderId,
        type: "LEFT",
        esf: data.get("leftEsf"),
        cil: data.get("leftCil"),
        axis: data.get("leftAxis"),
        addition: data.get("leftAddition"),
        dp: data.get("leftDp"),
        high: data.get("leftHigh"),
        avl: data.get("leftAvl"),
        avp: data.get("leftAvp")
      };
      await apiFetch(API.eyes, { method: "POST", body: JSON.stringify(leftEyeBody) });

      const rightEyeBody = {
        order: orderId,
        type: "RIGHT",
        esf: data.get("rightEsf"),
        cil: data.get("rightCil"),
        axis: data.get("rightAxis"),
        addition: data.get("rightAddition"),
        dp: data.get("rightDp"),
        high: data.get("rightHigh"),
        avl: data.get("rightAvl"),
        avp: data.get("rightAvp")
      };
      await apiFetch(API.eyes, { method: "POST", body: JSON.stringify(rightEyeBody) });

      showToast("Orden creada exitosamente");
    }

    ev.target.reset();
    delete els.orderForm.dataset.editingId;
    delete els.orderForm.dataset.leftEyeId;
    delete els.orderForm.dataset.rightEyeId;
    els.orderSubmit.textContent = "Crear orden completa";
    setOrdersView("list");
    await loadOrders();
  } catch (err) {
    showToast(err.message || "Error al guardar orden");
  } finally {
    els.orderSubmit.disabled = false;
    els.orderSubmit.textContent = els.orderForm.dataset.editingId ? "Actualizar orden" : "Crear orden completa";
  }
});

els.orderReset?.addEventListener("click", () => {
  els.orderForm.reset();
  delete els.orderForm.dataset.editingId;
  delete els.orderForm.dataset.leftEyeId;
  delete els.orderForm.dataset.rightEyeId;
  els.orderSubmit.textContent = "Crear orden completa";
});

// Users
els.userForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const data = new FormData(ev.target);
  const idHeadquarter = numberOrNull(data.get("idHeadquarter"));
  const body = {
    nit: data.get("nit"),
    name: data.get("name"),
    email: data.get("email"),
    password: data.get("password"),
    role: numberOrNull(data.get("role"))
  };
  if (idHeadquarter) {
    body.idHeadquarter = idHeadquarter;
  }
  try {
    await apiFetch(API.users, { method: "POST", body: JSON.stringify(body) });
    showToast("Usuario creado");
    ev.target.reset();
    setUsersView("list");
    loadUsers();
  } catch (err) {
    showToast(err.message || "Error creando usuario");
  }
});

els.roleForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const data = new FormData(ev.target);
  const body = { id: numberOrNull(data.get("id")), name: data.get("name") };
  try {
    await apiFetch(API.roles, { method: "POST", body: JSON.stringify(body) });
    showToast("Rol creado");
    ev.target.reset();
    loadRoles();
  } catch (err) {
    showToast(err.message || "Error creando rol");
  }
});

els.hqForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const data = new FormData(ev.target);
  const body = { idHeadquarter: numberOrNull(data.get("idHeadquarter")), name: data.get("name") };
  const editingId = els.hqForm?.dataset.editingId;
  try {
    if (editingId) {
      await apiFetch(`${API.headquarters}/${editingId}`, { method: "PATCH", body: JSON.stringify(body) });
      showToast("Sede actualizada");
    } else {
      await apiFetch(API.headquarters, { method: "POST", body: JSON.stringify(body) });
      showToast("Sede creada");
    }
    ev.target.reset();
    delete els.hqForm?.dataset.editingId;
    if (els.hqForm.querySelector("button[type='submit']")) {
      els.hqForm.querySelector("button[type='submit']").textContent = "Crear sede";
    }
    setHqView("list");
    loadHeadquarters();
  } catch (err) {
    showToast(err.message || "Error creando sede");
  }
});

els.providerForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const data = new FormData(ev.target);
  const body = {
    nit: data.get("nit"),
    name: data.get("name"),
    address: data.get("address"),
    email: data.get("email")
  };
  try {
    await apiFetch(API.providers, { method: "POST", body: JSON.stringify(body) });
    showToast("Proveedor creado");
    ev.target.reset();
    setProvidersView("list");
    loadProviders();
  } catch (err) {
    showToast(err.message || "Error creando proveedor");
  }
});

els.refreshUsers?.addEventListener("click", loadUsers);
els.refreshRoles?.addEventListener("click", loadRoles);
els.refreshHeadquarters?.addEventListener("click", loadHeadquarters);
els.refreshProviders?.addEventListener("click", loadProviders);
els.refreshPayments?.addEventListener("click", loadPayments);
els.paymentFilterOrder?.addEventListener("input", renderPayments);
els.hqFilterId?.addEventListener("input", renderHeadquarters);
els.hqSort?.addEventListener("change", renderHeadquarters);

els.paymentForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const fd = new FormData(els.paymentForm);
  const payment = {
    paymentDate: fd.get("paymentDate"),
    idOrder: Number(fd.get("idOrder")),
    amount: Number(fd.get("amount")),
    voucher: fd.get("voucher") || null
  };
  try {
    await apiFetch(API.payments, { method: "POST", body: JSON.stringify(payment) });
    showToast("Pago creado");
    els.paymentForm.reset();
    setPaymentsView("list");
    loadPayments();
  } catch (err) {
    showToast(err.message || "Error creando pago");
  }
});

// Logout
els.logoutBtn?.addEventListener("click", async () => {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
  localStorage.removeItem("roles");
  state.token = null;
  state.username = null;
  state.roles = [];
  flags.admin = false;
  flags.assistant = false;
  flags.provider = false;
  setTimeout(() => {
    window.location.href = "/login.html";
  }, 100);
});

function setUsersView(mode = "list") {
  const list = els.usersListView;
  const form = els.userFormPanel;
  const roles = els.rolesView;
  if (list) list.classList.toggle("hidden", mode !== "list");
  if (form) form.classList.toggle("hidden", mode !== "create");
  if (roles) roles.classList.toggle("hidden", mode !== "roles");

  const toggleBtn = (btn, active) => {
    if (!btn) return;
    btn.classList.toggle("primary", active);
    btn.classList.toggle("ghost", !active);
  };

  toggleBtn(els.btnUsersList, mode === "list");
  toggleBtn(els.btnUsersCreate, mode === "create");
  toggleBtn(els.btnUsersRoles, mode === "roles");

  if (mode === "roles") {
    loadRoles();
  } else {
    loadUsers();
  }
}

function setPaymentsView(mode = "list") {
  const list = els.paymentsListView;
  const form = els.paymentFormPanel;
  if (list) list.classList.toggle("hidden", mode !== "list");
  if (form) form.classList.toggle("hidden", mode !== "create");

  const toggleBtn = (btn, active) => {
    if (!btn) return;
    btn.classList.toggle("primary", active);
    btn.classList.toggle("ghost", !active);
  };

  toggleBtn(els.btnPaymentsList, mode === "list");
  toggleBtn(els.btnPaymentsCreate, mode === "create");

  if (mode === "list") renderPayments();
}

function setOrdersView(mode = "list") {
  const list = els.ordersListView;
  const form = els.orderFormPanel;
  if (list) list.classList.toggle("hidden", mode !== "list");
  if (form) form.classList.toggle("hidden", mode !== "create");

  const toggleBtn = (btn, active) => {
    if (!btn) return;
    btn.classList.toggle("primary", active);
    btn.classList.toggle("ghost", !active);
  };

  toggleBtn(els.btnOrdersList, mode === "list");
  toggleBtn(els.btnOrdersCreate, mode === "create");

  if (mode === "list") renderOrders();
}

function setHqView(mode = "list") {
  const list = els.hqListView;
  const form = els.hqFormPanel;
  if (list) list.classList.toggle("hidden", mode !== "list");
  if (form) form.classList.toggle("hidden", mode !== "create");

  const toggleBtn = (btn, active) => {
    if (!btn) return;
    btn.classList.toggle("primary", active);
    btn.classList.toggle("ghost", !active);
  };

  toggleBtn(els.btnHqList, mode === "list");
  toggleBtn(els.btnHqCreate, mode === "create");

  if (mode === "list") renderHeadquarters();
}

function setProvidersView(mode = "list") {
  const list = els.providersListView;
  const form = els.providerFormPanel;
  if (list) list.classList.toggle("hidden", mode !== "list");
  if (form) form.classList.toggle("hidden", mode !== "create");

  const toggleBtn = (btn, active) => {
    if (!btn) return;
    btn.classList.toggle("primary", active);
    btn.classList.toggle("ghost", !active);
  };

  toggleBtn(els.btnProvidersList, mode === "list");
  toggleBtn(els.btnProvidersCreate, mode === "create");

  if (mode === "list") renderProviders();
}

// Toggle entre lista y formulario de órdenes
els.btnOrdersList?.addEventListener("click", () => setOrdersView("list"));
els.btnOrdersCreate?.addEventListener("click", () => setOrdersView("create"));
els.exportOrders?.addEventListener("click", () => exportOrders());
els.orderCancel?.addEventListener("click", () => {
  els.orderForm?.reset();
  delete els.orderForm.dataset.editingId;
  delete els.orderForm.dataset.leftEyeId;
  delete els.orderForm.dataset.rightEyeId;
  els.orderSubmit.textContent = "Crear orden completa";
  setOrdersView("list");
  loadOrders();
});

// Toggle entre lista y formulario de sedes
els.btnHqList?.addEventListener("click", () => setHqView("list"));
els.btnHqCreate?.addEventListener("click", () => setHqView("create"));
els.hqCancel?.addEventListener("click", () => {
  els.hqForm?.reset();
  if (els.hqForm) delete els.hqForm.dataset.editingId;
  if (els.hqForm?.querySelector("button[type='submit']")) {
    els.hqForm.querySelector("button[type='submit']").textContent = "Crear sede";
  }
  setHqView("list");
  loadHeadquarters();
});

els.btnProvidersList?.addEventListener("click", () => setProvidersView("list"));
els.btnProvidersCreate?.addEventListener("click", () => setProvidersView("create"));
els.providerCancel?.addEventListener("click", () => {
  els.providerForm?.reset();
  setProvidersView("list");
  loadProviders();
});

els.btnUsersList?.addEventListener("click", () => setUsersView("list"));
els.btnUsersCreate?.addEventListener("click", () => setUsersView("create"));
els.btnUsersRoles?.addEventListener("click", () => setUsersView("roles"));
els.userCancel?.addEventListener("click", () => {
  els.userForm?.reset();
  setUsersView("list");
  loadUsers();
});

// Toggle entre lista y formulario de pagos
els.btnPaymentsList?.addEventListener("click", () => setPaymentsView("list"));
els.btnPaymentsCreate?.addEventListener("click", () => setPaymentsView("create"));
els.exportPayments?.addEventListener("click", () => exportPayments());
els.paymentCancel?.addEventListener("click", () => {
  els.paymentForm?.reset();
  setPaymentsView("list");
  loadPayments();
});

// Toggle entre lista y formulario de sedes
els.btnHqList?.addEventListener("click", () => {
  els.hqListView?.classList.remove("hidden");
  els.hqFormPanel?.classList.add("hidden");
  els.btnHqList.classList.remove("ghost");
  els.btnHqList.classList.add("primary");
  els.btnHqCreate?.classList.remove("primary");
  els.btnHqCreate?.classList.add("ghost");
});

els.btnHqCreate?.addEventListener("click", () => {
  els.hqListView?.classList.add("hidden");
  els.hqFormPanel?.classList.remove("hidden");
  els.btnHqCreate.classList.remove("ghost");
  els.btnHqCreate.classList.add("primary");
  els.btnHqList?.classList.remove("primary");
  els.btnHqList?.classList.add("ghost");
});

// Cerrar modal de ojos
els.closeEyesModal?.addEventListener("click", () => {
  els.eyesModal?.classList.add("hidden");
});

els.eyesModal?.addEventListener("click", (ev) => {
  if (ev.target === els.eyesModal) {
    els.eyesModal.classList.add("hidden");
  }
});

// Cerrar modal de estado
els.closeStateModal?.addEventListener("click", () => {
  els.stateModal?.classList.add("hidden");
});

els.stateModal?.addEventListener("click", (ev) => {
  if (ev.target === els.stateModal) {
    els.stateModal.classList.add("hidden");
  }
});

// Cerrar modal de recibido
els.closeReceivedModal?.addEventListener("click", () => {
  els.receivedModal?.classList.add("hidden");
});

els.receivedModal?.addEventListener("click", (ev) => {
  if (ev.target === els.receivedModal) {
    els.receivedModal.classList.add("hidden");
  }
});

els.receivedCancel?.addEventListener("click", () => {
  els.receivedModal?.classList.add("hidden");
});

// Cerrar modal de costo
els.closeCostModal?.addEventListener("click", () => {
  els.costModal?.classList.add("hidden");
});

els.costModal?.addEventListener("click", (ev) => {
  if (ev.target === els.costModal) {
    els.costModal.classList.add("hidden");
  }
});

els.costCancel?.addEventListener("click", () => {
  els.costModal?.classList.add("hidden");
});

els.costForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const orderId = els.costForm.dataset.orderId;
  const newCost = els.costNew?.value;
  const labVoucher = els.labVoucher?.value?.trim() || null;
  const parsed = Number(newCost);
  
  if (!Number.isFinite(parsed) || parsed < 0) {
    showToast("Costo no válido");
    return;
  }
  
  const payload = { costPrice: parsed };
  if (labVoucher) {
    payload.labVoucher = labVoucher;
  }
  
  try {
    await apiFetch(`${API.orders}/${orderId}`, { method: "PATCH", body: JSON.stringify(payload) });
    showToast("Costo actualizado");
    els.costModal?.classList.add("hidden");
    loadOrders();
  } catch (err) {
    showToast(err.message || "No se pudo actualizar el costo");
  }
});

els.receivedForm?.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const orderId = els.receivedForm.dataset.orderId;
  const receivedBy = els.receivedBy?.value?.trim();
  
  if (!receivedBy) {
    showToast("Debe ingresar el nombre de quien recibió");
    return;
  }
  
  try {
    const today = new Date().toISOString().split('T')[0];
    await apiFetch(`${API.orders}/${orderId}`, { 
      method: "PATCH", 
      body: JSON.stringify({ state: "RECEIVED", receivedBy, stateDate: today }) 
    });
    showToast("Orden marcada como recibida");
    els.receivedModal?.classList.add("hidden");
    loadOrders();
  } catch (err) {
    showToast(err.message || "No se pudo actualizar la orden");
  }
});

async function loadOrders() {
  try {
    state.orders = await apiFetch(API.orders);
    if (!state.patients.length) {
      await loadPatients();
    }
    renderOrders();
    updateDashboard();
    updateHeadquarterBlocks();
    updateReports();
  } catch (err) {
    setStatus(err.message || "Error cargando órdenes", false);
  }
}

async function loadHeadquarters() {
  try {
    state.headquarters = await apiFetch(API.headquarters);
    // Asegurar que las órdenes estén cargadas para el conteo
    if (!state.orders || state.orders.length === 0) {
      await loadOrders();
    }
    renderHeadquarters();
    populateHeadquarterSelects();
    updateHeadquarterBlocks();
  } catch (err) {
    showToast(err.message || "Error cargando sedes");
  }
}

async function loadUsers() {
  if (!flags.admin) return;
  try {
    state.users = await apiFetch(API.users);
    renderUsers();
  } catch (err) {
    showToast(err.message || "Error cargando usuarios");
  }
}

async function loadRoles() {
  if (!flags.admin) return;
  try {
    state.rolesData = await apiFetch(API.roles);
    renderRoles();
  } catch (err) {
    showToast(err.message || "Error cargando roles");
  }
}

async function loadProviders() {
  try {
    state.providers = await apiFetch(API.providers);
    renderProviders();
    populateProviderSelects();
  } catch (err) {
    showToast(err.message || "Error cargando proveedores");
  }
}

async function loadPatients() {
  try {
    state.patients = await apiFetch(API.patients);
  } catch (err) {
    showToast(err.message || "Error cargando pacientes");
  }
}

async function loadPayments() {
  try {
    state.payments = await apiFetch(API.payments);
    renderPayments();
    populatePaymentSelects();
  } catch (err) {
    showToast(err.message || "Error cargando pagos");
  }
}

function populatePaymentSelects() {
  // Llenar órdenes
  const orderOptions = (state.orders || [])
    .filter(o => o && o.idOrder)
    .map(o => `<option value="${o.idOrder}">Orden #${o.number}</option>`)
    .join("");
  if (els.paymentOrderSelect) {
    els.paymentOrderSelect.innerHTML = `<option value="">Seleccionar...</option>${orderOptions}`;
  }
}

function renderPayments() {
  const rows = filterPaymentsData()
    .map(p => {
      const order = (state.orders || []).find(o => `${o.idOrder}` === `${p.idOrder}`);
      const orderNum = order?.number || p.idOrder;
      return `<tr>
        <td>${p.idPayment}</td>
        <td>#${orderNum}</td>
        <td>${formatDate(p.paymentDate)}</td>
        <td>$${formatMoney(p.amount)}</td>
        <td>${p.voucher || '-'}</td>
        <td>
          <button class="ghost" data-action="delete-payment" data-id="${p.idPayment}">Eliminar</button>
        </td>
      </tr>`;
    })
    .join("");
  
  const header = `<tr><th>ID</th><th>Orden</th><th>Fecha</th><th>Monto</th><th>Comprobante</th><th>Acciones</th></tr>`;
  els.paymentsTable.innerHTML = rows ? `<table>${header}${rows}</table>` : "<p class='muted'>Sin pagos registrados</p>";
  
  els.paymentsTable.querySelectorAll("[data-action='delete-payment']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      if (!confirm(`Eliminar pago ${id}?`)) return;
      try {
        await apiFetch(`${API.payments}/${id}`, { method: "DELETE" });
        showToast("Pago eliminado");
        loadPayments();
      } catch (err) {
        showToast(err.message || "Error eliminando pago");
      }
    });
  });
}

function exportPayments() {
  const list = filterPaymentsData();
  const headers = ["ID", "Orden", "Fecha", "Monto", "Comprobante"];

  const rows = list.map(p => {
    const order = (state.orders || []).find(o => `${o.idOrder}` === `${p.idOrder}`);
    const orderNum = order?.number || p.idOrder || "";
    const amount = numberOrNull(p.amount) ?? p.amount ?? "";
    return [
      p.idPayment ?? "",
      orderNum,
      formatDate(p.paymentDate),
      amount,
      p.voucher || ""
    ];
  });

  downloadCSV("pagos.csv", headers, rows);
  showToast("Pagos exportados");
}

function getHeadquarterName(id) {
  if (id == null) return "";
  const hq = (state.headquarters || []).find(h => `${h.idHeadquarter}` === `${id}`);
  return hq?.name || id;
}

function getProviderName(id) {
  if (id == null) return "";
  const provider = (state.providers || []).find(p => `${p.idProvider}` === `${id}`);
  return provider?.name || id;
}

function getPatientName(documentId) {
  if (!documentId) return "";
  const patient = (state.patients || []).find(p => `${p.document}` === `${documentId}`);
  return patient?.name || documentId;
}

function formatDate(value) {
  if (!value) return "";
  // Accept both date-only and ISO date-time strings
  const str = `${value}`;
  if (str.length >= 10) return str.slice(0, 10);
  return str;
}

function formatMoney(value) {
  if (value === null || value === undefined || value === "") return "";
  const num = Number(value);
  if (!Number.isFinite(num)) return `${value}`;
  return num.toLocaleString('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function filterOrdersData() {
  const orders = [...(state.orders || [])].sort((a, b) => {
    const da = a?.creationDate ? new Date(a.creationDate).getTime() : 0;
    const db = b?.creationDate ? new Date(b.creationDate).getTime() : 0;
    if (db !== da) return db - da;
    const ida = Number(a.idOrder ?? a.id ?? 0);
    const idb = Number(b.idOrder ?? b.id ?? 0);
    return idb - ida;
  });
  
  // Si es proveedor puro (no admin/assistant), filtrar por proveedor
  let filtered = orders;
  if (flags.provider && !flags.admin && !flags.assistant) {
    // Encontrar el proveedor que coincida con el nombre del usuario
    const provider = (state.providers || []).find(p => p.name === state.username);
    if (provider) {
      filtered = orders.filter(o => Number(o.idProvider) === Number(provider.idProvider));
    } else {
      filtered = []; // Si no encuentra el proveedor, no mostrar órdenes
    }
  }
  
  const search = (els.filterSearch?.value || "").toLowerCase();
  const stateFilter = els.filterState?.value;
  return filtered.filter(o => {
    const matchesState = stateFilter ? o.state === stateFilter : true;
    const patientName = getPatientName(o.documentPatient).toLowerCase();
    const matchesSearch = search ? (
      (o.number?.toLowerCase().includes(search)) ||
      (o.documentPatient?.toLowerCase().includes(search)) ||
      patientName.includes(search)
    ) : true;
    return matchesState && matchesSearch;
  });
}

function filterPaymentsData() {
  const filterOrder = (els.paymentFilterOrder?.value || "").trim();
  return (state.payments || []).filter(p => {
    if (!filterOrder) return true;
    const idOrderStr = `${p.idOrder ?? ""}`;
    const order = (state.orders || []).find(o => `${o.idOrder}` === `${p.idOrder}`);
    const orderNum = order?.number ? `${order.number}` : "";
    return idOrderStr.includes(filterOrder) || orderNum.includes(filterOrder);
  });
}

function downloadCSV(filename, headers, rows) {
  const wrap = (value) => {
    if (value == null) return "";
    const str = `${value}`.replace(/"/g, '""');
    return `"${str}"`;
  };
  const lines = [headers.map(wrap).join(","), ...rows.map(r => r.map(wrap).join(","))];
  const blob = new Blob([lines.join("\n")], { type: "text/csv;charset=utf-8;" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = filename;
  link.click();
  URL.revokeObjectURL(url);
}

function renderOrders() {
  const filtered = filterOrdersData();

  const showFinancial = flags.admin;
  const showSalePrice = flags.admin || flags.assistant;

  const rows = filtered.map(order => {
    const tag = renderStateTag(order.state);
    const idVal = order.idOrder ?? order.id ?? "-";
    const actions = [];
    const canEdit = (flags.admin || flags.assistant) && idVal !== "-";
    const canDelete = flags.admin && idVal !== "-";
    const providerOnly = flags.provider && !flags.admin && !flags.assistant && idVal !== "-";

    if (canEdit) {
      actions.push(`<button class="ghost" data-action="edit" data-id="${idVal}">Editar</button>`);
    }
    if (canDelete) {
      actions.push(`<button class="ghost" data-action="delete" data-id="${idVal}">Eliminar</button>`);
    }
    if (providerOnly) {
      actions.push(`<button class="primary" data-action="change-state" data-id="${idVal}">Cambiar estado</button>`);
      actions.push(`<button class="ghost" data-action="update-cost" data-id="${idVal}">Actualizar costo</button>`);
    }
    const saleRaw = numberOrNull(order.salePrice);
    const costRaw = numberOrNull(order.costPrice);
    const utilRaw = (saleRaw != null && costRaw != null) ? (saleRaw - costRaw) : null;

    const sale = formatMoney(saleRaw);
    const cost = formatMoney(costRaw);
    const util = formatMoney(utilRaw);

    return `<tr>
      <td>${idVal}</td>
      <td>${formatDate(order.creationDate)}</td>
      <td>${getPatientName(order.documentPatient)}</td>
      <td>${order.number || ""}</td>
      <td>${order.labVoucher || ""}</td>
      <td>${getProviderName(order.idProvider)}</td>
      <td>${getHeadquarterName(order.idHeadquarter)}</td>
      <td>${order.lensType || ""}</td>
      <td><button class="ghost" data-action="view-eyes" data-id="${idVal}" style="padding: 6px 12px;" title="Ver ojos"><i class="bi bi-eye-fill" style="font-size: 18px;"></i></button></td>
      <td>${tag}</td>
      <td>${order.stateDate ? formatDate(order.stateDate) : ""}</td>
      ${showSalePrice ? `<td>$${sale}</td>` : ''}
      ${showFinancial ? `<td>$${cost}</td><td>${util ? `$${util}` : ""}</td>` : ''}
      <td class="actions-cell">${actions.join(" ") || ""}</td>
    </tr>`;
  }).join("");

  const headerFinancial = showFinancial
    ? `<th>Precio venta</th><th>Precio costo</th><th>Utilidad</th>`
    : (showSalePrice ? `<th>Precio venta</th>` : ``);

  els.ordersTable.innerHTML = `<table>
    <thead>
      <tr>
        <th>Consecutivo</th><th>Fecha</th><th>Paciente</th><th>Fac. Dr Mejia</th><th>Fac. Venta Lab</th><th>Proveedor</th><th>Sede</th><th>Tipo Lente (Asesor)</th><th>Fórmula</th><th>Estado</th><th>Fecha estado</th>${headerFinancial}<th></th>
      </tr>
    </thead>
    <tbody>${rows || ""}</tbody>
  </table>`;

  attachOrderActions();
}

function exportOrders() {
  const list = filterOrdersData();
  const showFinancial = flags.admin;
  const showSalePrice = flags.admin || flags.assistant;
  const headers = showFinancial
    ? ["Número", "Montura", "Tipo lente", "Precio venta", "Precio costo", "Utilidad", "Creada", "Paciente", "Sede", "Proveedor", "Envío", "Entrega", "Días", "Estado"]
    : (showSalePrice ? ["Número", "Montura", "Tipo lente", "Precio venta", "Creada", "Paciente", "Sede", "Proveedor", "Envío", "Entrega", "Días", "Estado"] : ["Número", "Montura", "Tipo lente", "Precio costo", "Creada", "Paciente", "Sede", "Proveedor", "Envío", "Entrega", "Días", "Estado"]);

  const rows = list.map(order => {
    const sale = numberOrNull(order.salePrice);
    const cost = numberOrNull(order.costPrice);
    const util = (sale != null && cost != null) ? (sale - cost) : "";

    const common = [
      order.number || "",
      order.frameType || "",
      order.lensType || ""
    ];

    const tail = [
      formatDate(order.creationDate),
      getPatientName(order.documentPatient),
      getHeadquarterName(order.idHeadquarter),
      getProviderName(order.idProvider),
      order.shippingDate || "",
      order.deliveryDate || "",
      order.daysPassed ?? "",
      translateState(order.state) || order.state || ""
    ];

    if (showFinancial) {
      const saleVal = sale ?? order.salePrice ?? "";
      const costVal = cost ?? order.costPrice ?? "";
      return [...common, saleVal, costVal, util, ...tail];
    }

    if (showSalePrice) {
      const saleVal = sale ?? order.salePrice ?? "";
      return [...common, saleVal, ...tail];
    }

    const costVal = cost ?? order.costPrice ?? "";
    return [...common, costVal, ...tail];
  });

  downloadCSV("ordenes.csv", headers, rows);
  showToast("Órdenes exportadas");
}

function translateState(state) {
  const translations = {
    CREATED: "Creada",
    SENT_TO_LAB: "Enviado a lab",
    IN_PROCESS: "En proceso",
    RETURNING: "Retorno en ruta",
    RECEIVED: "Recibido",
    CANCELLED: "Anulado"
  };
  return translations[state] || state;
}

function renderStateTag(state) {
  if (!state) return "";
  const clsMap = {
    CREATED: "pending",
    SENT_TO_LAB: "pending",
    IN_PROCESS: "progress",
    RETURNING: "progress",
    RECEIVED: "completed",
    CANCELLED: "cancelled"
  };
  const cls = clsMap[state] || "pending";
  return `<span class="tag ${cls}">${translateState(state)}</span>`;
}

function attachOrderActions() {
  els.ordersTable.querySelectorAll("[data-action='edit']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      const order = (state.orders || []).find(o => `${o.idOrder}` === `${id}` || `${o.id}` === `${id}`);
      if (!order) return;
      await fillOrderForm(order, id);
    });
  });
  els.ordersTable.querySelectorAll("[data-action='delete']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      if (!confirm(`Eliminar orden ${id}?`)) return;
      try {
        await apiFetch(`${API.orders}/${id}`, { method: "DELETE" });
        showToast("Orden eliminada");
        loadOrders();
      } catch (err) {
        showToast(err.message || "Error eliminando orden");
      }
    });
  });
  els.ordersTable.querySelectorAll("[data-action='change-state']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      const order = (state.orders || []).find(o => `${o.idOrder}` === `${id}` || `${o.id}` === `${id}`);
      const current = order?.state || "CREATED";
      const canSetReceived = flags.admin || flags.assistant;
      
      // Proveedores solo ven estados de laboratorio
      let states;
      if (flags.provider) {
        states = ["SENT_TO_LAB", "RETURNING", "CANCELLED"];
      } else {
        states = ["CREATED", "SENT_TO_LAB", "IN_PROCESS", "RETURNING", "RECEIVED", "CANCELLED"];
      }
      
      const stateButtons = states.map(st => {
        const isActive = st === current ? " active" : "";
        const isDisabled = (st === "RECEIVED" && !canSetReceived) ? " disabled" : "";
        return `<button class="state-btn${isActive}${isDisabled}" data-state="${st}" ${isDisabled ? 'disabled' : ''}>${translateState(st)}</button>`;
      }).join("");
      els.stateModalBody.innerHTML = `
        <p class="muted small">Estado actual: <strong>${translateState(current)}</strong></p>
        <div class="state-buttons" style="display: grid; gap: 0.5rem;">
          ${stateButtons}
        </div>
      `;
      els.stateModal.classList.remove("hidden");
      
      els.stateModalBody.querySelectorAll(".state-btn:not([disabled])").forEach(stateBtn => {
        stateBtn.addEventListener("click", async () => {
          const newState = stateBtn.dataset.state;
          
          // Si cambia a RECEIVED y es admin/assistant, pedir nombre
          if (newState === "RECEIVED" && canSetReceived) {
            els.stateModal.classList.add("hidden");
            if (els.receivedBy) els.receivedBy.value = "";
            els.receivedForm.dataset.orderId = id;
            els.receivedModal?.classList.remove("hidden");
            return;
          }
          
          try {
            const today = new Date().toISOString().split('T')[0];
            await apiFetch(`${API.orders}/${id}`, { method: "PATCH", body: JSON.stringify({ state: newState, stateDate: today }) });
            showToast("Estado actualizado");
            els.stateModal.classList.add("hidden");
            loadOrders();
          } catch (err) {
            showToast(err.message || "No se pudo actualizar");
          }
        });
      });
    });
  });

  // Actualizar costo (proveedor)
  els.ordersTable.querySelectorAll("[data-action='update-cost']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      const order = (state.orders || []).find(o => `${o.idOrder}` === `${id}` || `${o.id}` === `${id}`);
      const current = order?.costPrice ?? "";
      const currentVoucher = order?.labVoucher ?? "";
      
      // Mostrar modal con formulario
      if (els.costCurrent) els.costCurrent.value = current;
      if (els.costNew) els.costNew.value = current;
      if (els.labVoucher) els.labVoucher.value = currentVoucher;
      els.costForm.dataset.orderId = id;
      els.costModal?.classList.remove("hidden");
    });
  });
  
  // Botón ver ojos
  els.ordersTable.querySelectorAll("[data-action='view-eyes']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      await showEyesModal(id);
    });
  });
}

async function showEyesModal(orderId) {
  try {
    // Obtener todos los ojos y filtrar por orden
    const allEyes = await apiFetch(API.eyes);
    const orderEyes = allEyes.filter(eye => `${eye.order}` === `${orderId}`);
    const canEditEyes = flags.admin || flags.assistant;
    const canDeleteEyes = flags.admin;
    
    if (orderEyes.length === 0) {
      els.eyesModalBody.innerHTML = `<p class="muted">No hay ojos registrados para esta orden.</p>`;
    } else {
      const leftEye = orderEyes.find(e => e.type === "LEFT");
      const rightEye = orderEyes.find(e => e.type === "RIGHT");
      
      let html = "";
      const renderEyeSection = (eye, title) => {
        return `
          <div class="eye-section" data-eye-id="${eye.idEye}">
            <h5>${title}</h5>
            <div class="eye-grid">
              <div class="eye-field"><label>ESF</label><span>${eye.esf || "-"}</span></div>
              <div class="eye-field"><label>CIL</label><span>${eye.cil || "-"}</span></div>
              <div class="eye-field"><label>Eje</label><span>${eye.axis || "-"}</span></div>
              <div class="eye-field"><label>Adición</label><span>${eye.addition || "-"}</span></div>
              <div class="eye-field"><label>DP</label><span>${eye.dp || "-"}</span></div>
              <div class="eye-field"><label>Altura</label><span>${eye.high || "-"}</span></div>
              <div class="eye-field"><label>AVL</label><span>${eye.avl || "-"}</span></div>
              <div class="eye-field"><label>AVP</label><span>${eye.avp || "-"}</span></div>
            </div>
          </div>
        `;
      };

      if (leftEye) {
        html += renderEyeSection(leftEye, '<i class="bi bi-eye-fill"></i> Ojo Izquierdo');
      }
      
      if (rightEye) {
        html += renderEyeSection(rightEye, '<i class="bi bi-eye-fill"></i> Ojo Derecho');
      }
      
      els.eyesModalBody.innerHTML = html;
    }
    
    els.eyesModal.classList.remove("hidden");
  } catch (err) {
    showToast("Error cargando información de ojos");
    console.error(err);
  }
}

async function editEye(orderEyes, orderId, eyeId) {
  const eye = orderEyes.find(e => `${e.idEye}` === `${eyeId}`);
  if (!eye) return;

  const fields = {
    esf: "ESF",
    cil: "CIL",
    axis: "Eje",
    addition: "Adición",
    dp: "DP",
    high: "Altura",
    avl: "AVL",
    avp: "AVP"
  };

  const payload = { order: eye.order || orderId, type: eye.type };
  for (const [key, label] of Object.entries(fields)) {
    const val = prompt(`${label} (${eye.type === "LEFT" ? "Ojo Izquierdo" : "Ojo Derecho"})`, eye[key] || "");
    if (val === null) return;
    const trimmed = val.trim();
    if (!trimmed) {
      showToast("Todos los campos son obligatorios");
      return;
    }
    payload[key] = trimmed;
  }

  try {
    await apiFetch(`${API.eyes}/${eyeId}`, { method: "PUT", body: JSON.stringify(payload) });
    showToast("Ojo actualizado");
    await showEyesModal(orderId);
  } catch (err) {
    showToast(err.message || "No se pudo actualizar el ojo");
  }
}

async function deleteEye(orderId, eyeId) {
  if (!confirm(`¿Eliminar ojo ${eyeId}?`)) return;
  try {
    await apiFetch(`${API.eyes}/${eyeId}`, { method: "DELETE" });
    showToast("Ojo eliminado");
    await showEyesModal(orderId);
  } catch (err) {
    showToast(err.message || "No se pudo eliminar el ojo");
  }
}

async function fillOrderForm(order, id) {
  // Mostrar panel de formulario
  els.ordersListView?.classList.add("hidden");
  els.orderFormPanel?.classList.remove("hidden");
  if (els.btnOrdersCreate && els.btnOrdersList) {
    els.btnOrdersCreate.classList.remove("ghost");
    els.btnOrdersCreate.classList.add("primary");
    els.btnOrdersList.classList.remove("primary");
    els.btnOrdersList.classList.add("ghost");
  }

  els.orderForm.dataset.editingId = id;
  els.orderSubmit.textContent = "Actualizar orden";

  const patient = (state.patients || []).find(p => `${p.document}` === `${order.documentPatient}`);

  els.orderForm.number.value = order.number || "";
  els.orderForm.documentPatient.value = order.documentPatient || "";
  els.orderForm.patientName.value = patient?.name || "";
  els.orderForm.patientEmail.value = patient?.email || "";
  els.orderForm.patientAddress.value = patient?.address || "";
  els.orderForm.patientNotes.value = patient?.notes || "";
  els.orderForm.idHeadquarter.value = order.idHeadquarter || "";
  els.orderForm.idProvider.value = order.idProvider || "";
  if (els.orderForm.salePrice) els.orderForm.salePrice.value = order.salePrice ?? "";
  if (els.orderForm.costPrice) els.orderForm.costPrice.value = order.costPrice ?? "";
  els.orderForm.frameType.value = order.frameType || "";
  els.orderForm.lensType.value = order.lensType || "";
  els.orderForm.shippingDate.value = order.shippingDate || "";
  els.orderForm.deliveryDate.value = order.deliveryDate || "";
  els.orderForm.daysPassed.value = order.daysPassed ?? "";
  els.orderForm.state.value = order.state || "CREATED";

  try {
    const eyes = await apiFetch(API.eyes);
    const orderEyes = eyes.filter(e => `${e.order}` === `${id}`);
    const leftEye = orderEyes.find(e => e.type === "LEFT");
    const rightEye = orderEyes.find(e => e.type === "RIGHT");

    if (leftEye) {
      els.orderForm.dataset.leftEyeId = leftEye.idEye;
      els.orderForm.leftEsf.value = leftEye.esf || "";
      els.orderForm.leftCil.value = leftEye.cil || "";
      els.orderForm.leftAxis.value = leftEye.axis || "";
      els.orderForm.leftAddition.value = leftEye.addition || "";
      els.orderForm.leftDp.value = leftEye.dp || "";
      els.orderForm.leftHigh.value = leftEye.high || "";
      els.orderForm.leftAvl.value = leftEye.avl || "";
      els.orderForm.leftAvp.value = leftEye.avp || "";
    } else {
      delete els.orderForm.dataset.leftEyeId;
      ["leftEsf","leftCil","leftAxis","leftAddition","leftDp","leftHigh","leftAvl","leftAvp"].forEach(name => {
        if (els.orderForm[name]) els.orderForm[name].value = "";
      });
    }

    if (rightEye) {
      els.orderForm.dataset.rightEyeId = rightEye.idEye;
      els.orderForm.rightEsf.value = rightEye.esf || "";
      els.orderForm.rightCil.value = rightEye.cil || "";
      els.orderForm.rightAxis.value = rightEye.axis || "";
      els.orderForm.rightAddition.value = rightEye.addition || "";
      els.orderForm.rightDp.value = rightEye.dp || "";
      els.orderForm.rightHigh.value = rightEye.high || "";
      els.orderForm.rightAvl.value = rightEye.avl || "";
      els.orderForm.rightAvp.value = rightEye.avp || "";
    } else {
      delete els.orderForm.dataset.rightEyeId;
      ["rightEsf","rightCil","rightAxis","rightAddition","rightDp","rightHigh","rightAvl","rightAvp"].forEach(name => {
        if (els.orderForm[name]) els.orderForm[name].value = "";
      });
    }
  } catch (err) {
    console.error("No se pudieron cargar ojos para la orden", err);
    delete els.orderForm.dataset.leftEyeId;
    delete els.orderForm.dataset.rightEyeId;
  }

  showToast("Modo edición activo");
}

function renderHeadquarters() {
  const search = (els.hqFilterId?.value || "").toLowerCase();
  const sort = els.hqSort?.value || "id";
  const isAdmin = flags.admin;

  // Conteo de órdenes por sede
  const counts = (state.orders || []).reduce((acc, o) => {
    const key = o.idHeadquarter;
    if (!key && key !== 0) return acc;
    acc[key] = (acc[key] || 0) + 1;
    return acc;
  }, {});

  const list = (state.headquarters || []).filter(hq =>
    search ? `${hq.idHeadquarter}`.toLowerCase().includes(search) : true
  ).sort((a, b) => {
    if (sort === "orders") {
      const ca = counts[a.idHeadquarter] || 0;
      const cb = counts[b.idHeadquarter] || 0;
      return cb - ca;
    }
    if (sort === "name") {
      return (a.name || "").localeCompare(b.name || "");
    }
    // default sort by id asc
    return `${a.idHeadquarter}`.localeCompare(`${b.idHeadquarter}`);
  });

  const rows = list.map(hq => {
    const actions = [];
    if (isAdmin) {
      actions.push(`<button class="ghost" data-action="edit-hq" data-id="${hq.idHeadquarter}">Editar</button>`);
      actions.push(`<button class="ghost" data-action="delete-hq" data-id="${hq.idHeadquarter}">Eliminar</button>`);
    }
    return `<tr>
      <td>${hq.idHeadquarter}</td>
      <td>${hq.name}</td>
      <td>${counts[hq.idHeadquarter] || 0}</td>
      <td class="actions-cell">${actions.join(" ")}</td>
    </tr>`;
  }).join("");
  els.headquartersTable.innerHTML = `<table><thead><tr><th>ID</th><th>Nombre</th><th>Órdenes</th><th></th></tr></thead><tbody>${rows}</tbody></table>`;
  attachHeadquarterActions();
}

function translateRoleName(roleName) {
  const translations = {
    'admin': 'Administrador',
    'assistant': 'Asistente',
    'provider': 'Proveedor',
    'ROLE_ADMIN': 'Administrador',
    'ROLE_ASSISTANT': 'Asistente',
    'ROLE_PROVIDER': 'Proveedor'
  };
  return translations[roleName] || roleName;
}

function renderUsers() {
  const rows = (state.users || []).map(u => {
    const role = (state.rolesData || []).find(r => r.id === u.role);
    const roleName = role ? translateRoleName(role.name) : u.role;
    return `<tr>
      <td>${u.nit}</td><td>${u.name}</td><td>${u.email}</td><td>${roleName}</td>
      <td class="actions-cell"><button class="ghost" data-action="delete-user" data-id="${u.nit}">Eliminar</button></td>
    </tr>`;
  }).join("");
  els.usersTable.innerHTML = `<table><thead><tr><th>NIT</th><th>Nombre</th><th>Email</th><th>Rol</th><th></th></tr></thead><tbody>${rows}</tbody></table>`;
  attachUserActions();
}

function renderRoles() {
  const rows = (state.rolesData || []).map(r => `<tr><td>${r.id}</td><td>${translateRoleName(r.name)}</td></tr>`).join("");
  els.rolesTable.innerHTML = `<table><thead><tr><th>ID</th><th>Nombre</th></tr></thead><tbody>${rows}</tbody></table>`;
  populateRoleSelect();
}

function renderProviders() {
  const rows = (state.providers || []).map(p => `<tr>
    <td>${p.idProvider}</td><td>${p.nit}</td><td>${p.name}</td><td>${p.address}</td><td>${p.email}</td>
    <td class="actions-cell"><button class="ghost" data-action="delete-provider" data-id="${p.idProvider}">Eliminar</button></td>
  </tr>`).join("");
  els.providersTable.innerHTML = `<table><thead><tr><th>ID</th><th>NIT</th><th>Nombre</th><th>Dirección</th><th>Email</th><th></th></tr></thead><tbody>${rows}</tbody></table>`;
  attachProviderActions();
}

function populateRoleSelect() {
  if (!els.userRoleSelect) return;
  const options = (state.rolesData || []).map(r => `<option value="${r.id}">${translateRoleName(r.name)}</option>`).join("");
  els.userRoleSelect.innerHTML = `<option value="">Seleccione un rol</option>${options}`;
}

function populateHeadquarterSelects() {
  if (!els.orderHqSelect) return;
  const options = (state.headquarters || []).map(hq => `<option value="${hq.idHeadquarter}">${hq.name}</option>`).join("");
  els.orderHqSelect.innerHTML = `<option value="">Seleccione sede</option>${options}`;
  
  // Poblar también el select de usuarios
  if (els.userHqSelect) {
    els.userHqSelect.innerHTML = `<option value="">Sin sede</option>${options}`;
  }
}

function populateProviderSelects() {
  if (!els.orderProviderSelect) return;
  const options = (state.providers || []).map(p => `<option value="${p.idProvider}">${p.name}</option>`).join("");
  els.orderProviderSelect.innerHTML = `<option value="">Seleccione proveedor</option>${options}`;
}

function attachUserActions() {
  els.usersTable.querySelectorAll("[data-action='delete-user']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const nit = btn.dataset.id;
      if (!confirm(`¿Eliminar usuario con NIT ${nit}?`)) return;
      try {
        await apiFetch(`${API.users}/${nit}`, { method: "DELETE" });
        showToast("Usuario eliminado");
        loadUsers();
      } catch (err) {
        showToast(err.message || "Error eliminando usuario");
      }
    });
  });
}

function attachHeadquarterActions() {
  els.headquartersTable.querySelectorAll("[data-action='edit-hq']").forEach(btn => {
    btn.addEventListener("click", () => {
      const id = btn.dataset.id;
      const hq = (state.headquarters || []).find(h => `${h.idHeadquarter}` === `${id}`);
      if (!hq) return;

      if (els.hqForm) {
        els.hqForm.idHeadquarter.value = hq.idHeadquarter ?? "";
        els.hqForm.name.value = hq.name ?? "";
        els.hqForm.dataset.editingId = id;
        if (els.hqForm.querySelector("button[type='submit']")) {
          els.hqForm.querySelector("button[type='submit']").textContent = "Actualizar sede";
        }
      }

      setHqView("create");
    });
  });

  els.headquartersTable.querySelectorAll("[data-action='delete-hq']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      if (!confirm(`¿Eliminar sede ${id}?`)) return;
      try {
        await apiFetch(`${API.headquarters}/${id}`, { method: "DELETE" });
        showToast("Sede eliminada");
        loadHeadquarters();
      } catch (err) {
        showToast(err.message || "Error eliminando sede");
      }
    });
  });
}

function attachProviderActions() {
  els.providersTable.querySelectorAll("[data-action='delete-provider']").forEach(btn => {
    btn.addEventListener("click", async () => {
      const id = btn.dataset.id;
      if (!confirm(`¿Eliminar proveedor ${id}?`)) return;
      try {
        await apiFetch(`${API.providers}/${id}`, { method: "DELETE" });
        showToast("Proveedor eliminado");
        loadProviders();
      } catch (err) {
        showToast(err.message || "Error eliminando proveedor");
      }
    });
  });
}

function updateDashboard() {
  // Show provider header if user is a provider (not admin/assistant)
  const providerHeader = document.getElementById("provider-header");
  if (flags.provider && !flags.admin && !flags.assistant) {
    providerHeader.classList.remove("hidden");
  } else {
    providerHeader.classList.add("hidden");
  }
  
  let orders = state.orders || [];
  console.log("Dashboard - Total órdenes:", orders.length);
  console.log("Dashboard - flags.admin:", flags.admin);
  console.log("Dashboard - idHeadquarter:", state.idHeadquarter);
  // Filtrar por sede si el usuario no es admin
  if (!flags.admin && state.idHeadquarter) {
    console.log("Filtrando por idHeadquarter:", state.idHeadquarter);
    console.log("idHeadquarters en órdenes:", orders.map(o => o.idHeadquarter));
    orders = orders.filter(o => Number(o.idHeadquarter) === Number(state.idHeadquarter));
    console.log("Dashboard - Órdenes filtradas:", orders.length);
  }
  els.metricOrders.textContent = orders.length;
  const counts = countStates(orders);
  const pendingCount = (counts.CREATED || 0) + (counts.SENT_TO_LAB || 0);
  const progressCount = (counts.IN_PROCESS || 0) + (counts.RETURNING || 0);
  const completedCount = counts.RECEIVED || 0;
  els.metricPending.textContent = pendingCount;
  els.metricProgress.textContent = progressCount;
  els.metricCompleted.textContent = completedCount;
  els.metricOrdersSub.textContent = `${pendingCount} en trámite, ${progressCount} en proceso`;
  renderUpcoming(orders);
  renderStateBars(orders, els.stateBars);
}

function updateHeadquarterBlocks() {
  const orders = state.orders || [];
  const grouped = groupByHeadquarter(orders);
  const cards = Object.entries(grouped).map(([hq, list]) => {
    const counts = countStates(list);
    const pendingCount = (counts.CREATED || 0) + (counts.SENT_TO_LAB || 0);
    const progressCount = (counts.IN_PROCESS || 0) + (counts.RETURNING || 0);
    const completedCount = counts.RECEIVED || 0;
    const hqName = getHeadquarterName(hq);
    return `<article class="card">
      <p class="muted small">${hqName}</p>
      <h3>${list.length}</h3>
      <p class="tiny">${pendingCount} en trámite, ${progressCount} en proceso, ${completedCount} recibidas</p>
    </article>`;
  }).join("");
  els.headquarterCards.innerHTML = cards || "<p class='muted'>Sin datos de sedes</p>";
}

function updateReports() {
  let orders = state.orders || [];
  // Filtrar por sede si el usuario no es admin
  if (!flags.admin && state.idHeadquarter) {
    orders = orders.filter(o => Number(o.idHeadquarter) === Number(state.idHeadquarter));
  }
  const todayStr = new Date().toISOString().slice(0, 10);
  const todayCount = orders.filter(o => o.shippingDate === todayStr).length;
  els.metricToday.textContent = todayCount;

  const lateCount = orders.filter(o => {
    if (!o.deliveryDate || !o.shippingDate || o.daysPassed == null) return false;
    const ship = new Date(o.shippingDate);
    const due = new Date(ship.getTime() + (Number(o.daysPassed) || 0) * 86400000);
    return new Date(o.deliveryDate) > due;
  }).length;
  els.metricLate.textContent = lateCount;
  els.metricProviders.textContent = (state.providers || []).length;
  renderStateBars(orders, els.reportHqBars, true);
  renderSalesCharts(orders);
}

let salesChartInstance = null;
let revenueChartInstance = null;

function renderSalesCharts(orders) {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  const dates = [];
  const salesCounts = [];
  const revenueAmounts = [];
  
  for (let i = 19; i >= 0; i--) {
    const date = new Date(today);
    date.setDate(date.getDate() - i);
    const dateStr = date.toISOString().slice(0, 10);
    dates.push(dateStr);
    
    const dayOrders = orders.filter(o => o.creationDate && o.creationDate.slice(0, 10) === dateStr);
    salesCounts.push(dayOrders.length);
    
    const dayRevenue = dayOrders.reduce((sum, o) => {
      const sale = numberOrNull(o.salePrice);
      return sum + (sale ?? 0);
    }, 0);
    revenueAmounts.push(dayRevenue);
  }
  
  const labels = dates.map(d => {
    const [y, m, day] = d.split('-');
    return `${day}/${m}`;
  });
  
  const salesCanvas = document.getElementById('sales-chart');
  const revenueCanvas = document.getElementById('revenue-chart');
  
  if (salesCanvas) {
    if (salesChartInstance) salesChartInstance.destroy();
    salesChartInstance = new Chart(salesCanvas, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Cantidad de ventas',
          data: salesCounts,
          borderColor: '#082DA4',
          backgroundColor: 'rgba(8, 45, 164, 0.1)',
          borderWidth: 2,
          tension: 0.3,
          fill: true
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 1 }
          }
        }
      }
    });
  }
  
  if (revenueCanvas) {
    if (revenueChartInstance) revenueChartInstance.destroy();
    revenueChartInstance = new Chart(revenueCanvas, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Ingresos ($)',
          data: revenueAmounts,
          backgroundColor: 'rgba(251, 80, 18, 0.7)',
          borderColor: '#FB5012',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function(value) {
                return '$' + value.toLocaleString('es-CO');
              }
            }
          }
        }
      }
    });
  }
}

function renderUpcoming(orders) {
  const sorted = [...orders]
    .filter(o => o.shippingDate)
    .sort((a, b) => new Date(a.shippingDate) - new Date(b.shippingDate))
    .slice(0, 5);
  els.upcomingList.innerHTML = sorted.map(o => `<div class="list-item">
    <div>
      <strong>${o.number || "Sin número"}</strong>
      <p class="tiny">Paciente: ${o.documentPatient || "-"}</p>
    </div>
    <span>${o.shippingDate}</span>
  </div>`).join("") || "<p class='muted'>Sin datos</p>";
}

function renderStateBars(orders, container, byHq = false) {
  if (!container) return;
  if (byHq) {
    const group = groupByHeadquarter(orders);
    const total = orders.length || 1;
    container.innerHTML = Object.entries(group).map(([hq, list]) => {
      const pct = Math.round((list.length / total) * 100);
      const hqName = getHeadquarterName(hq);
      return `<div class="bar-row"><span>${hqName}</span><div class="bar"><div class="bar-fill" style="width:${pct}%"></div></div><span>${pct}%</span></div>`;
    }).join("") || "<p class='muted'>Sin datos</p>";
  } else {
    const counts = countStates(orders);
    const total = orders.length || 1;
    const entries = Object.entries(counts);
    container.innerHTML = entries.map(([state, count]) => {
      const pct = Math.round((count / total) * 100);
      return `<div class="bar-row"><span>${translateState(state)}</span><div class="bar"><div class="bar-fill" style="width:${pct}%"></div></div><span>${pct}%</span></div>`;
    }).join("") || "<p class='muted'>Sin datos</p>";
  }
}

function countStates(orders) {
  return orders.reduce((acc, o) => {
    if (!o.state) return acc;
    acc[o.state] = (acc[o.state] || 0) + 1;
    return acc;
  }, {});
}

function groupByHeadquarter(orders) {
  return orders.reduce((acc, o) => {
    const key = o.idHeadquarter ?? "-";
    acc[key] = acc[key] || [];
    acc[key].push(o);
    return acc;
  }, {});
}

function numberOrNull(value) {
  const n = Number(value);
  return Number.isFinite(n) ? n : null;
}

restoreSession();
