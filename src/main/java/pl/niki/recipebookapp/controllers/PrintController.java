package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrintController implements Initializable {
    // =================================================================================================================
    // Public fields - elements of the print-view.fxml
    // =================================================================================================================
    public ImageView pageImage;
    public Button printButton;
    public ChoiceBox<Printer> printerChoiceBox;
    public Spinner<Integer> copiesSpinner;
    public RadioButton landscapeRadioButton, portraitRadioButton, highRadioButton, normalRadioButton, lowRadioButton, colorRadioButton, monoRadioButton;
    public VBox vBox;

    // =================================================================================================================
    // Private fields
    // =================================================================================================================
    private final List<WritableImage> screenshotTab;

    // =================================================================================================================
    // Controllers
    // =================================================================================================================
    public PrintController(List<WritableImage> screenshot) {
        this.screenshotTab = screenshot;
    }

    // =================================================================================================================
    // Override methods
    // =================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ImageView ===================================================================================================

        // Set ImageView - first page
        pageImage.setImage(screenshotTab.get(0));

        // Set ImageViews - all pages
        for (WritableImage image: screenshotTab){
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(150);

            imageView.setOnMouseClicked(mouseEvent -> pageImage.setImage(imageView.getImage()));
            imageView.setCursor(Cursor.HAND);

            vBox.getChildren().add(imageView);
        }

        // Print Button ================================================================================================
        printButton.setOnAction(this::printAction);

        // Create and set Radio Button groups ==========================================================================
        final ToggleGroup orientationGroup = new ToggleGroup();
        landscapeRadioButton.setToggleGroup(orientationGroup);
        portraitRadioButton.setToggleGroup(orientationGroup);

        final ToggleGroup qualityGroup = new ToggleGroup();
        lowRadioButton.setToggleGroup(qualityGroup);
        normalRadioButton.setToggleGroup(qualityGroup);
        highRadioButton.setToggleGroup(qualityGroup);

        final ToggleGroup colorGroup = new ToggleGroup();
        colorRadioButton.setToggleGroup(colorGroup);
        monoRadioButton.setToggleGroup(colorGroup);

        // Printer ChoiceBox ===========================================================================================
        ObservableList<Printer> printersList = FXCollections.observableArrayList(Printer.getAllPrinters());
        printerChoiceBox.setItems(printersList);
        printerChoiceBox.getSelectionModel().select(Printer.getDefaultPrinter());

        // Amount of copies Spinner ====================================================================================
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1);
        copiesSpinner.setValueFactory(valueFactory);
    }

    // =================================================================================================================
    // Private methods
    // =================================================================================================================
    public void printAction(ActionEvent event) {

        // Create printer job ==========================================================================================
        final PrinterJob job = Objects.requireNonNull(PrinterJob.createPrinterJob(), "Cannot create printer job");

        // Get printer =================================================================================================
        final Printer printer = printerChoiceBox.getSelectionModel().getSelectedItem();
        job.setPrinter(printer);

        // Get amount of copies ========================================================================================
        job.getJobSettings().setCopies(copiesSpinner.getValue());

        // Get print quality ===========================================================================================
        PrintQuality quality;
        if (lowRadioButton.isSelected()){
            quality = PrintQuality.LOW;
        }
        else if (highRadioButton.isSelected()){
            quality = PrintQuality.HIGH;
        }
        else {
            quality = PrintQuality.NORMAL;
        }

        job.getJobSettings().setPrintQuality(quality);

        // Get print color =============================================================================================
        PrintColor color;
        if (monoRadioButton.isSelected()){
            color = PrintColor.MONOCHROME;
        }
        else {
            color = PrintColor.COLOR;
        }

        job.getJobSettings().setPrintColor(color);

        // Get page orientation ========================================================================================
        PageOrientation orientation;
        if (landscapeRadioButton.isSelected()){
            orientation = PageOrientation.LANDSCAPE;
        }
        else {
            orientation = PageOrientation.PORTRAIT;
        }

        // create page layout - set paper, page orientation and margin type
        final PageLayout pageLayout = printer.createPageLayout(Paper.A4, orientation, Printer.MarginType.HARDWARE_MINIMUM);
        job.getJobSettings().setPageLayout(pageLayout);

        // Scale image to page =========================================================================================
        final double scaleX = pageLayout.getPrintableWidth() / screenshotTab.get(0).getWidth();
//        final double scaleY = pageLayout.getPrintableHeight() / screenshot.getHeight();

        Scale scale;
        scale = new Scale(scaleX, scaleX);

        // Print =======================================================================================================
        boolean success = false;
        for (WritableImage writableImage : screenshotTab) {
            final ImageView print_node = new ImageView(writableImage);
            print_node.getTransforms().add(scale);

            success = job.printPage(print_node);

            print_node.getTransforms().remove(scale);
        }
        if (success) {
            job.endJob();
            ((Stage) printButton.getScene().getWindow()).close();
        }
    } // Print Button Action
}
