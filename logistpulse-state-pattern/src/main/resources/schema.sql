CREATE TABLE IF NOT EXISTS inventory_batches (
    batch_id VARCHAR(50) PRIMARY KEY,
    sku VARCHAR(50) NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('RECIBIDO', 'ALMACENADO', 'RESERVADO', 'ENVIADO', 'CUARENTENA'))
);
