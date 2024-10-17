module com.ticketbooking.cinema {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
   // requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    //requires com.jfoenix;

    opens com.ticketbooking.cinema to javafx.fxml;
    exports com.ticketbooking.cinema;

    opens models to javafx.base;
}
