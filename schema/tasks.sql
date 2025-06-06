
CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL CHECK (due_date >= CURRENT_DATE),
    priority VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    completed BOOLEAN DEFAULT FALSE
);
