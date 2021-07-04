package com.diksha.employeedata.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diksha.employeedata.Modal.Employee
import com.diksha.employeedata.R
import com.squareup.picasso.Picasso

class EmployeeAdapter(var context: Context, employeeModelsroom: List<Employee>) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {
    var employeeModels: List<Employee>
    var type = "Room"
    var firstclick = false

    fun getAllMyModels(MyModelList: List<Employee>) {
        employeeModels = MyModelList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == "live") {
            val employeeModel = employeeModels[position]
            // bindview(employeeModel, holder, position);
        } else {
            bindviewroom(employeeModels[position], holder, position)
        }
    }

    override fun getItemCount(): Int {
        return if (type.equals(
                "Live",
                ignoreCase = true
            )
        ) employeeModels.size else employeeModels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearLayout: LinearLayout
        var linearLayoutcollapsed: LinearLayout
        var imageView: ImageView
        var imageViewprofile: ImageView
        var title: TextView
        var name: TextView
        var education: TextView
        var age: TextView
        var desig: TextView

        init {
            title = itemView.findViewById(R.id.textView)
            name = itemView.findViewById(R.id.name)
            education = itemView.findViewById(R.id.education)
            age = itemView.findViewById(R.id.agegender)
            desig = itemView.findViewById(R.id.desgnation_place)
            imageView = itemView.findViewById(R.id.profile_image)
            imageViewprofile = itemView.findViewById(R.id.profile_image_small)
            linearLayout = itemView.findViewById(R.id.expanded)
            linearLayoutcollapsed = itemView.findViewById(R.id.collaped)
        }
    }

    fun bindviewroom(employeeModel: Employee, holder: ViewHolder, position: Int) {

        holder.title.text = employeeModel.firstname + " " + employeeModel.lastname
        holder.name.text = employeeModel.firstname + " " + employeeModel.lastname
        holder.age.text = employeeModel.age + " " + employeeModel.gender
        holder.desig.text = employeeModel.role + " at " + employeeModel.org
        holder.education.text = employeeModel.degree + ", " + employeeModel.institution
        Picasso.get().load(employeeModel.picture).into(holder.imageView)
        Picasso.get().load(employeeModel.picture).into(holder.imageViewprofile)
        val expanded = employeeModel.isExpanded
        holder.linearLayout.visibility = if (expanded) View.VISIBLE else View.GONE
        holder.linearLayoutcollapsed.visibility = if (expanded) View.GONE else View.VISIBLE
        holder.linearLayout.startAnimation(
            if (expanded) AnimationUtils.loadAnimation(
                context, R.anim.slide_down
            ) else AnimationUtils.loadAnimation(
                context, R.anim.slide_up
            )
        )
        holder.linearLayoutcollapsed.setOnClickListener {
            val expanded = employeeModel.isExpanded
            employeeModel.isExpanded = !expanded
            notifyItemChanged(position)
        }
        holder.linearLayout.setOnClickListener {
            val expanded = employeeModel.isExpanded
            employeeModel.isExpanded = !expanded
            notifyItemChanged(position)
        }
    } //    void bindview(Employee employeeModel, ViewHolder holder, int position) {

    //
    //        holder.title.setText(employeeModel.getFirstname() + " " + employeeModel.getLastname());
    //        holder.name.setText(employeeModel.getFirstname() + " " + employeeModel.getLastname());
    //        holder.age.setText(employeeModel.getAge() + " " + employeeModel.getGender());
    //        holder.desig.setText(employeeModel.getJobholder().getRole() + " at " + employeeModel.getJobholder().getOrganization());
    //        holder.education.setText(employeeModel.getEducation().getDegree() + ", " + employeeModel.getEducation().getInstitution());
    //        Picasso.get().load(employeeModel.getPicture()).into(holder.imageView);
    //        Picasso.get().load(employeeModel.getPicture()).into(holder.imageViewprofile);
    //
    //        boolean expanded = employeeModel.isExpanded();
    //        holder.linearLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
    //        holder.linearLayoutcollapsed.setVisibility(expanded ? View.GONE : View.VISIBLE);
    //        holder.linearLayout.startAnimation(expanded ?
    //                AnimationUtils.loadAnimation(context, R.anim.slide_down) : AnimationUtils.loadAnimation(context, R.anim.slide_up));
    //
    //        holder.linearLayoutcollapsed.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                boolean expanded = employeeModel.isExpanded();
    //                employeeModel.setExpanded(!expanded);
    //                notifyItemChanged(position);
    //            }
    //        });
    //        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                boolean expanded = employeeModel.isExpanded();
    //                employeeModel.setExpanded(!expanded);
    //                notifyItemChanged(position);
    //
    //            }
    //        });
    //    }
    //    public EmployeeAdapter(Context context, List<Maindata> employeeModels, String type) {
    //        this.context = context;
    //        this.type = type;
    //        this.employeeModels = employeeModels;
    //    }
    init {
        type = type
        employeeModels = employeeModelsroom
    }
}