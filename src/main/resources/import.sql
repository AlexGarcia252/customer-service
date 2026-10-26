INSERT INTO customers (id, name, id_document, email, status) VALUES (1, 'Juan Perez', '1098765432', 'juan.perez@test.com', 'ACTIVE');
INSERT INTO customers (id, name, id_document, email, status) VALUES (2, 'Maria Gomez', '1012345678', 'maria.gomez@test.com', 'ACTIVE');
ALTER SEQUENCE customers_SEQ RESTART WITH 3;