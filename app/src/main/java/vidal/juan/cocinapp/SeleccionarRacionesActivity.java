package vidal.juan.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SeleccionarRacionesActivity extends AppCompatActivity {

    private ListView lista;
    private Button botonPedir;
    private ArrayList<EncapsuladorEntradas> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pedido);

        // Referencias a los elementos en activity_realizar_pedido.xml
        lista = findViewById(R.id.lista);
        botonPedir = findViewById(R.id.botonPedir);

        datos = new ArrayList<>();

        datos.add(new EncapsuladorEntradas(R.drawable.tortilla, "TORTILLA DE PATATAS", "2.20", 1, 3));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "CROQUETAS", "2.40", 2, 1));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "PAELLA", "3.00", 3, 5));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "CALAMARES", "3.50", 2, 7));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "AJOARRIERO", "4.50", 4, 16));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "TORTILLA FRANCESA", "2.00", 5, 3));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "BOLA DE PIMIENTOS", "2.30", 3, 8));
        datos.add(new EncapsuladorEntradas(R.drawable.icono_anadir, "BOLA DE HUEVO", "2.30", 2, 10));

        // Establece todas las cantidades a 0
        for (EncapsuladorEntradas entrada : datos) {
            entrada.setCantidadActual(0);
        }


        lista.setAdapter(new AdaptadorEntradas(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(EncapsuladorEntradas entrada, View view) {
                if (entrada != null) {
                    TextView titulo_entrada = view.findViewById(R.id.titulo_entrada);
                    TextView precio_entrada = view.findViewById(R.id.precio_entrada);
                    ImageView imagen_entrada = view.findViewById(R.id.imagen);

                    Button botonAnadir = view.findViewById(R.id.botonAnadir);
                    Button botonQuitar = view.findViewById(R.id.botonQuitar);
                    TextView textoCantidad = view.findViewById(R.id.textoCantidad);

                    // Configura los listeners para los botones
                    botonAnadir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            incrementarCantidad(entrada, textoCantidad);
                            actualizarBotonPedir();
                        }
                    });

                    botonQuitar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decrementarCantidad(entrada, textoCantidad);
                            actualizarBotonPedir();
                        }
                    });

                    titulo_entrada.setText(entrada.get_textoTitulo());
                    precio_entrada.setText(entrada.get_Precio());
                    imagen_entrada.setImageResource(entrada.get_idImagen());
                    textoCantidad.setText(String.valueOf(entrada.getCantidadActual()));
                }
            }
        });

        // Configurar listener para el botón Pedir
        botonPedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la cantidad total y el precio total
                int cantidadTotal = obtenerCantidadTotal(datos);
                double precioTotal = obtenerPrecioTotal(datos);

                // Iniciar la actividad HacerPedido (ajusta el nombre de la actividad según sea necesario)
                Intent intent = new Intent(SeleccionarRacionesActivity.this, HacerPedidoActivity.class);
                startActivity(intent);
            }
        });

// Ocultar el botón Pedir inicialmente
        botonPedir.setVisibility(View.GONE);
    }

    private void incrementarCantidad(EncapsuladorEntradas entrada, TextView textoCantidad) {
        int cantidadActual = entrada.getCantidadActual();
        int cantidadMaxima = entrada.getCantidadMaxima();
        int stock = entrada.getStock();

        if (cantidadActual < Math.min(cantidadMaxima, stock)) {
            entrada.setCantidadActual(cantidadActual + 1);
            actualizarTextView(entrada, textoCantidad);
        } else {
            if (cantidadActual == cantidadMaxima) {
                mostrarMensaje("Se ha alcanzado el máximo de productos de este tipo por pedido");
            } else {
                mostrarMensaje("Se ha alcanzado el límite de productos disponibles en stock");
            }
        }
    }

    private void mostrarMensaje(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void decrementarCantidad(EncapsuladorEntradas entrada, TextView textoCantidad) {
        if (entrada.getCantidadActual() > 0) {
            entrada.setCantidadActual(entrada.getCantidadActual() - 1);
            actualizarTextView(entrada, textoCantidad);
        }
    }

    private void actualizarTextView(EncapsuladorEntradas entrada, TextView textoCantidad) {
        textoCantidad.setText(String.valueOf(entrada.getCantidadActual()));
    }

    private void actualizarBotonPedir() {
        // Verificar si hay al menos un producto con cantidad mayor que 0
        boolean mostrarBoton = false;
        for (EncapsuladorEntradas entrada : datos) {
            if (entrada.getCantidadActual() > 0) {
                mostrarBoton = true;
                actualizarTextoBoton();
                break;
            }
        }

        // Mostrar u ocultar el botón Pedir según sea necesario
        botonPedir.setVisibility(mostrarBoton ? View.VISIBLE : View.GONE);
    }

    private int obtenerCantidadTotal(ArrayList<EncapsuladorEntradas> datos) {
        int cantidadTotal = 0;
        for (EncapsuladorEntradas entrada : datos) {
            cantidadTotal += entrada.getCantidadActual();
        }
        return cantidadTotal;
    }

    private double obtenerPrecioTotal(ArrayList<EncapsuladorEntradas> datos) {
        double precioTotal = 0;
        for (EncapsuladorEntradas entrada : datos) {
            precioTotal += entrada.getCantidadActual() * Double.parseDouble(entrada.get_Precio());
        }
        return precioTotal;
    }

    private void actualizarTextoBoton() {
        // Verificar si hay al menos un producto con cantidad mayor que 0
        boolean mostrarBoton = false;
        for (EncapsuladorEntradas entrada : datos) {
            if (entrada.getCantidadActual() > 0) {
                mostrarBoton = true;
                break;
            }
        }

        // Mostrar u ocultar el botón Pedir según sea necesario
        botonPedir.setVisibility(mostrarBoton ? View.VISIBLE : View.GONE);

        // Actualizar el texto del botón con la cantidad total y el precio total
        if (mostrarBoton) {
            int cantidadTotal = obtenerCantidadTotal(datos);
            double precioTotal = obtenerPrecioTotal(datos);
            String textoBoton = "Pedir " + cantidadTotal + " por " + String.format("%.2f", precioTotal) + " €";
            botonPedir.setText(textoBoton);
        }
    }


}
