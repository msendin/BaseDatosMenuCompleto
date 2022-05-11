package cat.udl.eps.bbdd2;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText txtNombre;
	private EditText txtTel;
	private EditText txtMail;
	private TextView txtResultado;

	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Obtenemos las referencias a los controles
		txtNombre = findViewById(R.id.txtNom);
		txtTel = findViewById(R.id.txtTel);
		txtMail = findViewById(R.id.txtMail);
		txtResultado = findViewById(R.id.txtResultado);

		Button btnInsertar = findViewById(R.id.btnInsertar);
		Button btnActualizar = findViewById(R.id.btnActualizar);
		Button btnEliminar = findViewById(R.id.btnEliminar);
		Button btnConsultar = findViewById(R.id.btnConsultar);
		Button btnCons1 = findViewById(R.id.btnConsultar1);
		
		//Abrimos la base de datos 'DBUsuarios' en modo escritura
        UsuariosSQLiteHelper usdbh =
            new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);
 
        db = usdbh.getWritableDatabase();
		
		btnInsertar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String tel = txtTel.getText().toString();
				String mail = txtMail.getText().toString();
				
				ContentValues nuevoRegistro = new ContentValues();
				nuevoRegistro.put("nombre", nom);
				nuevoRegistro.put("telefono", tel);
				nuevoRegistro.put("email", mail);
				if (db.insert("Usuarios", null, nuevoRegistro) > 0)
					showToast(getString(R.string.correctIn));
				else
					showToast(getString(R.string.errIn));
			}
		});
		
		btnActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String tel = txtTel.getText().toString();
				String mail = txtMail.getText().toString();
				
				String[] args = new String[]{nom};
				
				ContentValues valores = new ContentValues();
				valores.put("telefono", tel);
				valores.put("email", mail);
				int cont = db.update("Usuarios", valores, "nombre=?", args);
				if (cont > 0)
					showToast(getString(R.string.correctAct)  + Integer.toString(cont));
				else
					showToast(getString(R.string.noReg));
			}
		});
		
		btnEliminar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Recuperamos los valores de los campos de texto
				String nom = txtNombre.getText().toString();
				String[] args = new String[]{nom};				
				int cont = db.delete("Usuarios", "nombre=?", args);
				if (cont > 0)
					showToast(getString(R.string.correctEl) + Integer.toString(cont));
				else
					showToast(getString(R.string.noReg));
			}
		});
		
		btnConsultar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Alternativa 1: metodo rawQuery()
				Cursor c = db.rawQuery("SELECT nombre, telefono, email FROM Usuarios", null);
							
				//Alternativa 2: metodo query()
				//String[] campos = new String[] {"nombre", "telefono", "email"};
				//Cursor c = db.query("Usuarios", campos, null, null, null, null, null);
				
				//Recorremos los resultados para mostrarlos en pantalla
				txtResultado.setText("");
				if (c.moveToFirst()) {
				     //Recorremos el cursor hasta que no haya mas registros
				     do {
				          String nom = c.getString(0);
				          String tel = c.getString(1);
				          String mail = c.getString(2);
				          
				          txtResultado.append(" " + nom + " - " + tel + " - " + mail + "\n");
				     } while(c.moveToNext());
				}
				else 
					txtResultado.setText(R.string.tablaV);
				
			}
		});
		
		btnCons1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String[] campos = new String[]{"telefono", "email"};
				String nom = txtNombre.getText().toString();
				String[] args = new String[]{nom};
				Cursor c = db.query("Usuarios", campos, "nombre=?", args, null, null, null);

				txtResultado.setText("");

				//Alternativa 1: metodo rawQuery()
				//String nom = txtNombre.getText().toString();
				//String[] args = new String[]{nom};
				//Cursor c = db.rawQuery("SELECT telefono, email FROM Usuarios WHERE nombre=?", args);

				if (c.moveToFirst()) {
					//Recorremos el cursor hasta que no haya mas registros
					do {
						String tel = c.getString(0);
						String mail = c.getString(1);
						txtResultado.append(" " + tel + " - " + mail + "\n");
					} while (c.moveToNext());
				} else
					txtResultado.setText(R.string.noReg);
			}

		});
	}

	
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
	protected void onDestroy () {
        super.onDestroy();
        db.close();
    }
}
