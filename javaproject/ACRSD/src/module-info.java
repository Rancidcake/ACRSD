module ACRSD {
    // Enables JDBC (java.sql) and Swing/AWT (java.desktop)
    requires java.sql;
    requires java.desktop;

    // Export the main package (ensure your package statements use lowercase 'acrsd')
    exports acrds;
}