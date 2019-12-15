package controllers;

import Models.OEE;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class VentanaPricipalController implements Initializable {



   @FXML
   private JFXTextField txt_tiempoEstandardFabricacion;

   @FXML
   private JFXTextField txt_tiempoPorTurno;

   @FXML
   private JFXTextField txt_tiempoPlaneado;

   @FXML
   private JFXTextField txt_tiempoParadas;

   @FXML
   private JFXTextField txt_tiempoAislamiento;

   @FXML
   private JFXTextField txt_tiempoCambios;

   @FXML
   private JFXTextField txt_tiempoEsperas;

   @FXML
   private JFXTextField txt_produccionReal;

   @FXML
   private JFXTextField txt_numeroUnidadesDefectuosas;

   @FXML
   private JFXTextField txt_numeroUnidadesRemanufacturadas;

   @FXML
   private  JFXSpinner spinner_disponibilidad;

   @FXML
   private JFXSpinner spinner_eficiencia;

   @FXML
   private JFXSpinner spinner_calidad;

   @FXML
   private JFXSpinner spinner_OEE;

   private String[] listaMetricas = new String[]{"Horas","Minutor","Segundos"};
   @FXML
   private JFXRadioButton rb_horas;

   @FXML
   private ToggleGroup opcion_metrica;

   @FXML
   private JFXRadioButton rb_minutos;

   @FXML
   private JFXRadioButton rb_segundos;
   private ArrayList<JFXTextField> lista_txt = new ArrayList<>();
   private OEE oee;
   private Tooltip tooltipDisponibilidad = new Tooltip();
   private Tooltip tooltipEficiencia = new Tooltip();
   private Tooltip tooltipCalidad = new Tooltip();
   private Tooltip tooltipOEE = new Tooltip();

   @Override
   public void initialize(URL location, ResourceBundle resources) {


      lista_txt.add(this.txt_numeroUnidadesDefectuosas);
      lista_txt.add(this.txt_numeroUnidadesRemanufacturadas);
      lista_txt.add(this.txt_produccionReal);
      lista_txt.add(this.txt_tiempoAislamiento);
      lista_txt.add(this.txt_tiempoEsperas);
      lista_txt.add(this.txt_tiempoCambios);
      lista_txt.add(this.txt_tiempoParadas);
      lista_txt.add(this.txt_tiempoPlaneado);
      lista_txt.add(this.txt_tiempoPorTurno);
      lista_txt.add(this.txt_tiempoEstandardFabricacion);

      this.spinner_disponibilidad.setTooltip(this.tooltipDisponibilidad);
      this.spinner_eficiencia.setTooltip(this.tooltipEficiencia);
      this.spinner_calidad.setTooltip(this.tooltipCalidad);
      this.spinner_OEE.setTooltip(this.tooltipOEE);

      this.tooltipDisponibilidad.setText("0%");
      this.tooltipEficiencia.setText("0%");
      this.tooltipCalidad.setText("0%");
      this.tooltipOEE.setText("0%");

      for(JFXTextField jfxTextField : lista_txt){

         DoubleValidator doubleValidator = new DoubleValidator();
         RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();


         doubleValidator.setMessage("Solo números permitidos");
         requiredFieldValidator.setMessage("Este campo es requerido");

         jfxTextField.getValidators().add(requiredFieldValidator);
         jfxTextField.getValidators().add(doubleValidator);
         jfxTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
               @Override
               public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                  if(!newValue)
                     jfxTextField.validate();
               }
            });
         jfxTextField.textProperty().addListener(new ChangeListener<String>() {
               @Override
               public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

               }
            });

         jfxTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
               @Override
               public void handle(KeyEvent event) {
                  if(event.getCode() == KeyCode.ENTER){
                     jfxTextField.validate();
                     if(event.getSource() == txt_numeroUnidadesRemanufacturadas){
                        if(validar()){
                           calcular();
                        }

                     }
                  }
               }
            });

      }

      this.opcion_metrica.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
         @Override
         public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
               if(validar())
                  calcular();
           
               if(newValue == rb_horas)
                  System.out.println("yes");
           
         }
      });



   }

   private boolean validar() {

      boolean validado = true;
      for(JFXTextField jfxTextField : lista_txt){
         jfxTextField.validate();
         validado = validado && jfxTextField.validate();
      }


      return validado;
   }

   private void calcular() {

      this.oee = new OEE(
              convertir(this.txt_tiempoPorTurno.getText()),
              //tiempo muerto
              convertir(this.txt_tiempoParadas.getText()),
              convertir(this.txt_tiempoPlaneado.getText()),
              convertir(this.txt_tiempoAislamiento.getText()),
              convertir(this.txt_tiempoCambios.getText()),
              convertir(this.txt_tiempoEsperas.getText()),
              //fin tiempo muerto
              convertir(this.txt_tiempoEstandardFabricacion.getText()),
              //calidad
              convertir(this.txt_produccionReal.getText()),
              convertir(this.txt_numeroUnidadesDefectuosas.getText()),
              convertir(this.txt_numeroUnidadesRemanufacturadas.getText())
      );

     // spinner_disponibilidad.setProgress(oee.getDisponibilidad());
      //spinner_calidad.setProgress(oee.getCalidad());
      //spinner_eficiencia.setProgress(oee.getEficiencia());
      //spinner_OEE.setProgress(oee.getOEE());

      //animarProgreso(spinner_disponibilidad,oee.getDisponibilidad());
      //animarProgreso(spinner_calidad,oee.getCalidad());
     // animarProgreso(spinner_eficiencia,oee.getEficiencia());
      //animarProgreso(spinner_OEE,oee.getOEE());
      Timeline timeline = new Timeline(
              new KeyFrame(
                      Duration.seconds(1),
                      new KeyValue(spinner_disponibilidad.progressProperty(),oee.getDisponibilidad())
              ),
              new KeyFrame(
                      Duration.seconds(1),
                      new KeyValue(spinner_calidad.progressProperty(),oee.getCalidad())
              ),
              new KeyFrame(
                      Duration.seconds(1),
                      new KeyValue(spinner_eficiencia.progressProperty(),oee.getEficiencia())
              ),
              new KeyFrame(
                      Duration.seconds(1),
                      new KeyValue(spinner_OEE.progressProperty(),oee.getOEE())
              )
      );
      timeline.play();

      tooltipDisponibilidad.setText((oee.getDisponibilidad()*100)+"%");
      tooltipCalidad.setText((oee.getCalidad()*100)+"%");
      tooltipEficiencia.setText((oee.getEficiencia() *100)+"%");
      tooltipOEE.setText((oee.getOEE()*100)+"%");

   }

   private double convertir(String numero){
      return Double.parseDouble(numero);
   }


   private void animarProgreso(final JFXSpinner jfxSpinner, double progreso){

      double progress = progreso*100;

      Thread thread = new Thread(new Runnable() {
         int i = 0;
         @Override
         public void run() {
            while (i<=progress) {
               jfxSpinner.setProgress(i/100);
               /*
               Platform.runLater(new Runnable() {
                  @Override
                  public void run() {

                     jfxSpinner.setProgress(i/100);

                  }
               });

               */
               i++;
               try {
                  sleep(10);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }


            jfxSpinner.setProgress(progreso);
         }
      });

      thread.setDaemon(true);
      thread.start();
   }

}
