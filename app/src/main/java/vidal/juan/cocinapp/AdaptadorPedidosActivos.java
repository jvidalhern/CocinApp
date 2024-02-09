package vidal.juan.cocinapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class AdaptadorPedidosActivos extends BaseAdapter {

    private ArrayList<Pedido> pedidosActivos;
    private int R_layout_IdView;
    private Context contexto;

    public AdaptadorPedidosActivos(Context contexto, int R_Layout_IdView, ArrayList<Pedido> pedidosActivos) {
        super();
        this.contexto = contexto;
        this.pedidosActivos = pedidosActivos;
        this.R_layout_IdView = R_Layout_IdView;
    }

    public abstract void onEntrada(Pedido pedidoActivo, View view);

    @Override
    public int getCount() {
        return pedidosActivos.size();
    }

    @Override
    public Pedido getItem(int position) {
        return pedidosActivos.get(position);
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

        onEntrada(pedidosActivos.get(position), convertView);

        return convertView;
    }
}