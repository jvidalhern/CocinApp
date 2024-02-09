package vidal.juan.cocinapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class AdaptadorDetalles extends BaseAdapter {

    private ArrayList<DetallePedido> detallesPedido;
    private int R_layout_IdView;
    private Context contexto;

    public AdaptadorDetalles(Context contexto, int R_Layout_IdView, ArrayList<DetallePedido> detallesPedido) {
        super();
        this.contexto = contexto;
        this.detallesPedido = detallesPedido;
        this.R_layout_IdView = R_Layout_IdView;
    }

    public abstract void onEntrada(DetallePedido detallePedido, View view);

    @Override
    public int getCount() {
        return detallesPedido.size();
    }

    @Override
    public DetallePedido getItem(int position) {
        return detallesPedido.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)
                    contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R_layout_IdView, null);
        }

        onEntrada(detallesPedido.get(position), convertView);

        return convertView;
    }
}
