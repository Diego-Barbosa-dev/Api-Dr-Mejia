-- Seed roles
INSERT INTO roles (id, name) VALUES
  (1, 'admin'),
  (2, 'assistant'),
  (3, 'provider')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Seed admin user
INSERT INTO users (id_user, nit, name, email, password, id_rol, id_headquarter, created_at, active) VALUES
  (1, 'admin123', 'admin123', 'admin123@example.com', 'admin123', 1, NULL, NOW(), TRUE)
ON DUPLICATE KEY UPDATE
  nit = VALUES(nit),
  name = VALUES(name),
  email = VALUES(email),
  password = VALUES(password),
  id_rol = VALUES(id_rol),
  id_headquarter = VALUES(id_headquarter),
  created_at = VALUES(created_at),
  active = VALUES(active);
