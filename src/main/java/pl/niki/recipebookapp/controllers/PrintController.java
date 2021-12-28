package pl.niki.recipebookapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    public ImageView pageImage, backgroundView;
    public Button printButton;
    public ChoiceBox<Printer> printerChoiceBox;
    public Spinner<Integer> copiesSpinner;
    public RadioButton landscapeRadioButton, portraitRadioButton, highRadioButton, normalRadioButton, lowRadioButton, colorRadioButton, monoRadioButton;
    public VBox vBox;
    public HBox hBox;

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
        WritableImage img = new WritableImage(1, 1);
        PixelWriter pw = img.getPixelWriter();
        pw.setColor(0, 0, Color.WHITE);
        backgroundView.setImage(img);

        pageImage = new ImageView(screenshotTab.get(0));
        pageImage.setPreserveRatio(screenshotTab.get(0).getWidth() > screenshotTab.get(0).getHeight());
        pageImage.setFitWidth(372);
        pageImage.setFitHeight(525);
        pageImage.setBlendMode(BlendMode.MULTIPLY);

        Group blend = new Group(
                backgroundView,
                pageImage
        );
        hBox.getChildren().add(0,blend);

        // Set ImageViews - all pages
        for (WritableImage image: screenshotTab){
            ImageView bottom = new ImageView(img);
            bottom.setPreserveRatio(false);
            bottom.setFitHeight(200);
            bottom.setFitWidth(150);

            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(150);
            imageView.setBlendMode(BlendMode.MULTIPLY);
            imageView.setPreserveRatio(image.getHeight() < image.getWidth());

            Group imageGroup = new Group(
                    bottom,
                    imageView
            );
            imageGroup.setOnMouseClicked(mouseEvent -> pageImage.setImage(imageView.getImage()));
            vBox.getChildren().add(imageGroup);
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
