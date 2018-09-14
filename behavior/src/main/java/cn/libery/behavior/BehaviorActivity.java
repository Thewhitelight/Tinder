package cn.libery.behavior;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author shizhiqiang
 */
public class BehaviorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        RecyclerView recyclerView = findViewById(R.id.behavior_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                return new Holder(getLayoutInflater().inflate(R.layout.item_simple, parent, false));
            }
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Holder holder = (Holder) viewHolder;
                holder.title.setText("Title" + i);
                holder.content.setText("Content" + i);
            }

            @Override
            public int getItemCount() {
                return 30;
            }

            class Holder extends RecyclerView.ViewHolder {

                TextView title;
                TextView content;

                Holder(View itemView) {
                    super(itemView);
                    title = itemView.findViewById(R.id.title);
                    content = itemView.findViewById(R.id.content);
                }
            }
        });
    }
}
