package com.ues.api_demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton; // <--- CAMBIO IMPORTANTE
import android.widget.ListView;
import android.widget.TextView;   // Necesario para el adaptador
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.ColorStateList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Para los colores

import com.google.android.material.floatingactionbutton.FloatingActionButton; // <--- CAMBIO IMPORTANTE

import com.ues.api_demo.models.Product;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Productos extends AppCompatActivity {

    private ListView listView;
    private ImageButton btnLogout;           // <--- AHORA ES IMAGE BUTTON
    private FloatingActionButton btnAdd;     // <--- AHORA ES FAB
    private ApiService apiService;
    private String token;
    private List<Product> listaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos);

        listView = findViewById(R.id.listViewProducts);
        btnLogout = findViewById(R.id.btnLogout);
        btnAdd = findViewById(R.id.btnAdd);

        token = getIntent().getStringExtra("TOKEN");

        // TU IP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.43:8085/api/v1/demoapirestdam235/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        cargarProductos();

        // EVENTOS
        btnLogout.setOnClickListener(v -> cerrarSesion());
        btnAdd.setOnClickListener(v -> mostrarDialogoProducto(null));

        // Clic corto: EDITAR
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Product productoSeleccionado = listaActual.get(position);
            mostrarDialogoProducto(productoSeleccionado);
        });

        // Clic largo: ELIMINAR
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Product productoSeleccionado = listaActual.get(position);
            confirmarEliminacion(productoSeleccionado);
            return true;
        });
    }

    private void cargarProductos() {
        apiService.getProducts("Bearer " + token).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaActual = response.body();
                    // Usamos el adaptador personalizado "Chulada"
                    ProductAdapter adapter = new ProductAdapter(listaActual);
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(Productos.this, "Error carga: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- ADAPTADOR PERSONALIZADO ---
    class ProductAdapter extends ArrayAdapter<Product> {
        public ProductAdapter(List<Product> products) {
            super(Productos.this, 0, products);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Product producto = getItem(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_producto, parent, false);
            }

            // 1. BUSCAMOS LOS TEXTOS
            TextView txtNombre = convertView.findViewById(R.id.txtNombre);
            TextView txtCodigo = convertView.findViewById(R.id.txtCodigo);
            View statusIndicator = convertView.findViewById(R.id.statusIndicator);

            // 2. BUSCAMOS LOS BOTONES (Aquí es donde debes tener cuidado con los IDs del XML)
            View btnDelete = convertView.findViewById(R.id.btnDelete); // La Basura
            View btnEdit = convertView.findViewById(R.id.btnEdit);     // <--- EL LÁPIZ (Asegúrate que en el XML tenga este ID)

            // 3. SETEAMOS LOS DATOS
            txtNombre.setText(producto.getName());
            txtCodigo.setText("ID: " + producto.getCode());

            if (producto.isStatus()) {
                statusIndicator.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(Productos.this, R.color.active_green)));
            } else {
                statusIndicator.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(Productos.this, R.color.inactive_red)));
            }

            // 4. ACCIÓN BORRAR (Click en la basura)
            if (btnDelete != null) {
                btnDelete.setOnClickListener(v -> {
                    confirmarEliminacion(producto); // Llama a tu método de abajo
                });
            }

            // 5. ACCIÓN EDITAR (Click en el lápiz) <--- ESTO ES LO NUEVO
            if (btnEdit != null) {
                btnEdit.setOnClickListener(v -> {
                    mostrarDialogoProducto(producto); // Llama a tu método de abajo para abrir el cuadro de edición
                });
            }

            return convertView;
        }
    }

    // --- LÓGICA CRUD (Igual que antes) ---

    private void mostrarDialogoProducto(Product producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");

        final EditText input = new EditText(this);
        input.setHint("Nombre del producto");
        if (producto != null) {
            input.setText(producto.getName());
        }
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = input.getText().toString();
            if (!nombre.isEmpty()) {
                if (producto == null) {
                    crearProducto(nombre);
                } else {
                    producto.setName(nombre);
                    actualizarProducto(producto);
                }
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void crearProducto(String nombre) {
        Product nuevo = new Product(nombre, true);
        apiService.createProduct("Bearer " + token, nuevo).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Productos.this, "Creado!", Toast.LENGTH_SHORT).show();
                    cargarProductos();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(Productos.this, "Fallo al crear", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProducto(Product producto) {
        apiService.updateProduct("Bearer " + token, producto).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Productos.this, "Actualizado!", Toast.LENGTH_SHORT).show();
                    cargarProductos();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(Productos.this, "Fallo al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarEliminacion(Product producto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Borrar " + producto.getName() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    apiService.deleteProduct("Bearer " + token, producto.getCode()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(Productos.this, "Eliminado", Toast.LENGTH_SHORT).show();
                                cargarProductos();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(Productos.this, "Error al borrar", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cerrarSesion() {
        apiService.logout("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                salirDeLaApp();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                salirDeLaApp();
            }
        });
    }

    private void salirDeLaApp() {
        Intent intent = new Intent(Productos.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}