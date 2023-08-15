# VIEW BINDING TRONG ADAPTER CỦA RECYCLER VIEW

<hr />

## CÁCH SỬ DỤNG VIEW BINDING LIÊN KẾT UI COMPONENT TRONG LAYOUT VỚI DATA SOURCE TRONG APP

- thông thường để làm việc với các __View__ trên __Layout__ ta cần xác định thuộc tính __id__ thông qua method __findViewById()__, với cách này
    - quá trình xây dựng ứng dụng tốn thời gian
    - không được kiểm tra tại thời điểm biên dịch, nên khi xác định sai __id__ sẽ dẫn đến ứng dụng bị crash
- để khắc phục những nhược điểm trên, Google cung cấp giải pháp __View Binding__

- ví dụ ta có file layout __activity_main.xml__ như sau
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/btn_click"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:text="@string/open_fragment" />

    <FrameLayout
        android:id="@+id/content_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

<hr />

### CÁC BƯỚC THIẾT LẬP VÀ SỬ DỤNG VIEW BINDING

- Khai báo sử dụng __View Binding__ trong __build.gradle module:app__, trong thẻ __android__ ta thêm vào đoạn code bật __viewBinding__ là __true__, sau đó click __Sync Now__
```js
android {
    // 

    buildFeatures {
        viewBinding true
    }
}
```

> nếu muốn 1 __layout__ nào đó bỏ qua quá trình tạo class Binding (không sử dụng View Binding) ta thêm thuộc tính sau vào __root view__ của __layout__ đó
>> <LinearLayout
>>        ...
>>       tools:viewBindingIgnore="true" >
>>    ...
>> </LinearLayout>

- sau khi bật được __viewBinding__ thì __Android__ sẽ tự sinh ra các file class Binding cho các __layout__ có trong ứng dụng
- ví dụ: file __layout__ __activity_main.xml__ thì sẽ có file Binding là __ActivityMainBinding.java__
- ta sẽ khai báo đối tượng __Binding__ này trong file java tương ứng với layout
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // nơi khởi tạo đối tượng Binding
        setContentView(R.layout.activity_main);
}
```

> Lưu ý: TA CÓ THỂ KHÔNG CẦN KHAI BÁO BIỂN ĐỂ QUẢN LÝ LIÊN KẾT __id__ các View trên Layout

- sau khi khai báo, ta khởi tạo đối tượng Binding sau ``super.onCreate(savedInstanceState);`` đồng thời chỉnh sửa lại method ``setContentView()``
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView( mActivityMainBinding.getRoot());

        // hoặc có thể viết tách riêng đối tượng View dùng để set content cho giao diện
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = mActivityMainBinding.getRoot();
//        setContentView(view);        
}
```

- sử dụng đối tượng __Binding__ đã khởi tạo để gọi đến __id__ của các __View__ trên __Layout__ và có thể thực hiện thao tác trực tiếp các method
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mActivityMainBinding.getRoot();
        setContentView(view);
        
        mActivityMainBinding.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        });
    }
}
```

- hoặc có thể khai báo đối tượng View sau đó sử dụng đối tượng Binding gọi đến id để gán cho đối tượng View để thao tác gọn hơn, nhưng cách này cũng gần giống như cách sử dụng __findViewById()__

> khi ta sử dụng View Binding thì class Binding của các Layout tương ứng sẽ được sinh ra
>> các class này sẽ quản lý __id__ của các view trong layout tương ứng.
>> khi sử dụng đối tượng Binding, chỉ cần nhập dấu ``.`` phía sau đối tượng đó, thì danh sách id mà nó quản lý sẽ được xổ xuống cho ta chọn.
>> về quy tắc đặt id cho View trên Layout.
>>> phân biệt UPPERCASE và lowercase.
>>> dấu gạch nối ``_`` sẽ bị loại bỏ, những ký tự đầu sau dấu ``_`` sẽ được viết UPPERCASE.
>>> vì vậy tránh trường hợp khi sau khi bỏ đi dấu ``_`` ta có 2 id giống nhau trên layout, lúc này đối tượng Binding sẽ liên kết bị lỗi, xử lý logic sẽ lẫn lộn

<hr />

### BẮT ĐẦU VỚI FRAGMENT

- sau khi cài đặt View Binding, khởi tạo và sử dụng View Binding gọi đến View trong Layout, ở __MainActivity.java__ thông qua __AcitivtyMainBinding__ gọi đến đối View Button xử lý sự kiện người dùng click vào, ta tiến hành xây dựng hàm gọi replace Fragment lên FrameLayout.
- trước đó ta cần tạo Fragment trong project
- __MyFragment.java__
```java
public class MyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }
}
```
- __fragment_my.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/black"
    android:padding="14dp"
    tools:context=".MyFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:id="@+id/tv_name"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center_horizontal"
        android:text="@string/hello_my_fragment"
        android:textColor="@color/white"
        android:textSize="30sp" />

</FrameLayout>
```

- trong sự kiện click Button của MainActivity ta gọi đến method, method này sẽ thực hiện công việc replace Fragment lên FrameLayout
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mActivityMainBinding.getRoot();
        setContentView(view);
        
        mActivityMainBinding.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyFragment();
            }
        });
    }

    private void openMyFragment() {
        MyFragment myFragment = new MyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_layout, myFragment);
        transaction.commitAllowingStateLoss();
    }
}
```

<hr />

### VIEW BINDING VỚI FRAGMENT

- sau khi thực hiện các bước ở trên ta vẫn chưa sử dụng View Binding trên Fragment, ở đây có 1 đối tượng View trên Fragment là TextView, ta sẽ thực hiện View Binding với View này.
- tương tự như Activity có file layout đi kèm, Fragment cũng có file layout đi kèm, và khi ta bật View Binding trong ``build.gradle module:app`` thì 1 file class Binding với các layout có trong project sẽ được sinh ra, ở đây sau khi xây dựng được MyFragment thì ta sẽ có 1 class ``FragmentMyBinding`` tương ứng với layout ``fragment_my.xml`` để sử dụng cho việc Binding View trong __MyFragment__.
- cách sử dụng thì tương tự như trong __Activity__, chỉ khác là khi khởi tạo đối tượng ViewBinding sẽ khác đôi chút so với trong Activity
- ngoài ra trong Fragment đôi khi ta cần sử dụng đối tượng View của Fragment để thao tác, ta có thể khai báo đối tượng View toàn cục và khởi tạo đối tượng này để sử dụng cho toàn bộ Fragment
- __MyFragment.java__
```java
public class MyFragment extends Fragment {
    // đối tượng View Binding
    private FragmentMyBinding mFragmentMyBinding;

    // đối tượng instance View của Fragment
    private View mView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentMyBinding = FragmentMyBinding.inflate(inflater, container, false);

        mFragmentMyBinding.tvName.setText("TEST VIEW BINDING IN FRAGMENT");

        // khởi tạo đối tượng View và return về cho Fragment
        mView = mFragmentMyBinding.getRoot();
        return mView;
    }
}
```

<hr />

## SỬ DỤNG VIEW BINDING TRONG ADAPTER CỦA RECYCLER VIEW

### THÊM RECYCLER VIEW VÀO LAYOUT FRAGMENT

- ta sẽ sử dụng __Recycler View__ trên __MyFragment__, trong layout của Fragment ta thiết kế lại và thêm __Recycler View__ nằm trong __Layout__ của __Fragment__
- __fragment_my.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/blue"
    android:orientation="vertical"
    android:padding="14dp"
    tools:context=".MyFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:id="@+id/tv_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center_horizontal"
        android:text="@string/hello_my_fragment"
        android:textColor="@color/white"
        android:textSize="30sp" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rcv_user"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp" />

</LinearLayout>
```

### THIẾT KẾ LAYOUT ITEM

- chuột phải __layout__ chọn __New/XML/Layout XML file__
- đặt tên file layout là __item_user__ và thiết kế như sau
- __item_user.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:padding="20dp"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/tv_user_name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/app_name"
        android:textColor="@color/black"
        android:textSize="16sp"
        android:textStyle="bold" />

</LinearLayout>
```

### TẠO FILE OBJECT USER

- trước khi thiết kế file Adapter ta thiết kế class đối tượng User để Adapter thao tác đến
- __User.java__
```java
package com.example.viewbindingadapterrecyclerview;

public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### TẠO ADAPTER

- tạo 1 class Adapter quản lý User
- __UserAdapter.java__
```java
public class UserAdapter {
    
}
```

- tạo __Inner Class View Holder__ trong class __UserAdapter__ và __extends RecyclerView.ViewHolder__ sau đó thực hiện __override__ lại method của __RecyclerView.ViewHolder__
__UserAdapter.java__
```java
public class UserAdapter {

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    
}
```

- ở trong Inner Class __UserViewHolder__, bình thường ta sẽ khai báo các thành phần __View__ của __item_user.xml__ và thực hiện __Bind data__ thông qua method __findViewById()__
- nhưng ở đây ta đang sử dụng __View Binding__, và class __UserViewHolder__ quản lý giao diện của __item_user.xml__ nên ta sẽ sử dụng __Binding class__ của __item_user.xml__
- __UserAdapter.java__
```java
public class UserAdapter {

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding itemUserBinding;
        
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
```

- ở __Constructor method__ override lại từ __RecyclerView.ViewHolder__ ta sửa tham số đầu vào là Binding class __ItemUserBinding__, và trong method __super__ ta cần truyền vào 1 View nếu không sẽ xảy ra lỗi, mà View ở đây chính là tham số __ItemUserBinding.getRoot()__
- sau đó ta gán tham số đầu vào của __Constructor ViewHolder__ cho biến thành viên Binding class __UserViewHolder__
- __UserAdapter.java__
```java
public class UserAdapter {

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding itemUserBinding;

        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
            this.itemUserBinding = itemUserBinding;
        }
    }

}
```

- sau khi thiết lập xong Inner class __UserViewHolder__ trong Adapter class __UserAdapter__, ta thực hiện __extends RecyclerView.Adapter\<UserAdapter.UserViewHolder>__ với kiểu tham số là __UserViewHolder__ mà ta đã thiết kế
- sau đó ta tiến hành override các method của __RecyclerView.Adapter\<UserAdapter.UserViewHolder>__
- __UserAdapter.java__
```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding itemUserBinding;

        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
            this.itemUserBinding = itemUserBinding;
        }
    }
}
```

- sau khi override các method ta khai báo biến thành viên cho Adapter class __UserAdapter__, vì __UserAdapter__ sẽ làm việc với dữ liệu là các __User__ nên biến thành viên của nó có kiểu dữ liệu là __List\<User>__, đồng thời ta cũng khai báo method Constructor cho __UserAdapter__ với tham số truyền vào là __List\<User>__
- __UserAdapter.java__
```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> mUserList;

    public UserAdapter(List<User> mUserList) {
        this.mUserList = mUserList;
    }

    // ....
}
```

- bây giờ ta sẽ xử lý đối với 3 method đã override từ __extends RecyclerView.Adapter\<UserAdapter.UserViewHolder>__ trong __UserAdapter__
- __getItemCount()__ kiểm tra __List User__ mà khác ``null`` thì trả về kích thước của __List User__, nếu không thì ``return 0``
- __UserAdapter.java__
```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    //...

    @Override
    public int getItemCount() {
        if (mUserList != null) {
            return mUserList.size();
        }
        return 0;
    }

    //...
}
```

- __onCreateViewHolder(@NonNull ViewGroup parent, int viewType)__ giá trị trả về là đối tượng ViewHolder ở đây là __UserViewHolder__, mà Constructor của __UserViewHolder__ cần tham số là đối tượng Binding tương ứng với Item Layout của __UserViewHolder__
- trong method __onCreateViewHolder()__ khởi tạo đối tượng Binding __ItemUserBinding__ bằng method __inflate()__ với 3 tham số cho __inflate()__ là
    - Context: có được thông qua __LayoutInflater.from(parent.getContext())__
    - ViewGroup: là đối tượng ViewGroup trả về cho tham số đầu vào của __onCreateViewHolder()__
    - Boolean: mặc định là ``false``
- sau đó ``return`` về đối tượng __UserViewHolder__ bằng cách gọi Constructor của __UserViewHolder__ và truyền đối tượng __ItemUserBinding__ ta đã khởi tạo
- __UserAdapter.java__
```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    //...

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(itemUserBinding);
    }

    //...
}
```

- __onBindViewHolder(@NonNull UserViewHolder holder, int position)__ thực hiện __Bind__ dữ liệu lên __UserViewHolder__, ở method này nó truyền vào cho ta 2 tham số là đối tượng __ViewHolder__ và __position__ vị trí của __ViewHolder__ trên __RecyclerView__
- trong method này để thực hiện __Bind__ dữ liệu lên __ViewHolder__, mà dữ liệu ở đây chính là __getName()__ của đối tượng __User__, ta thực hiện trong method như sau
    - lấy đối tượng __User__ ở __position__ trong __mUserList__
    - kiểm tra đối tượng __User__ ở __position__ đó có ``null`` hay không, nếu ``null`` thì ``return;`` mà không làm gì
    - còn nếu không ``null`` thì thực hiện __bind__ dữ liệu lên __ViewHolder__ truyền vào cho method thông qua đối tượng __Binding__
- __UserAdapter.java__
```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    //...

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUserList.get(position);
        if (user == null) {
            return;
        }
        holder.itemUserBinding.tvUserName.setText(user.getName());
    }

    //...
}
```

- chỉnh sửa lại 1 số vị trí mà Android Studio warning, ta có 1 Adapter sử dụng kỹ thuật __ViewBinding__ như sau
- __UserAdapter.java__
```java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> mUserList;

    public UserAdapter(List<User> mUserList) {
        this.mUserList = mUserList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(itemUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUserList.get(position);
        if (user == null) {
            return;
        }
        holder.itemUserBinding.tvUserName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        if (mUserList != null) {
            return mUserList.size();
        }
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding itemUserBinding;

        public UserViewHolder(@NonNull ItemUserBinding itemUserBinding) {
            super(itemUserBinding.getRoot());
            this.itemUserBinding = itemUserBinding;
        }
    }
}
```

<hr />

## KHỞI ĐỘNG RECYCLER VIEW TRONG FRAGMENT

- quay trở lại __MyFragment__, ta sẽ khởi tạo __RecylerView__, __Adapter__ và thiết lập dữ liệu cho Adapter đổ lên RecyclerView
- sau khi lệnh thực hiện truy xuất View cho Fragment ``mView = mFragmentMyBinding.getRoot();`` thành công, lúc này ta sẽ thao tác với Adapter, RecyclerView thông qua đối tượng View này
- đối với RecyclerView
    - truy xuất ánh xạ RecyclerView thông qua class Binding của Fragment, vì RecyclerView là 1 View trong Fragment
    - khởi tạo LinearLayoutManager với tham số là View Fragment gọi getContext
    - setLayoutManager cho RecyclerView
- ta có thể thêm đường Divider (phân cách) và RecyclerView để phân cách các item
- đối với Adapter, khởi tạo và thêm dữ liệu vào Adapter
- có Adapter, RecyclerView, thực hiện setAdapter lên RecyclerView

- __MyFragment.java__
```java
public class MyFragment extends Fragment {
    // đối tượng View Binding
    private FragmentMyBinding mFragmentMyBinding;

    // đối tượng instance View của Fragment
    private View mView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentMyBinding = FragmentMyBinding.inflate(inflater, container, false);

        mFragmentMyBinding.tvName.setText("TEST VIEW BINDING IN FRAGMENT");

        // khởi tạo đối tượng View để return về cho Fragment
        mView = mFragmentMyBinding.getRoot();

        // sử dụng Fragment Binding ánh xạ cho đối tượng RecyclerView
        RecyclerView rcvUser = mFragmentMyBinding.rcvUser;
        // khởi tạo LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext());
        // setLayoutManager cho RecyclerView
        rcvUser.setLayoutManager(linearLayoutManager);

        // thêm đường trang trí phân cách cho mỗi item trên RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mView.getContext(), DividerItemDecoration.VERTICAL);
        rcvUser.addItemDecoration(dividerItemDecoration);

        // khởi tạo UserAdapter
        UserAdapter userAdapter = new UserAdapter(getUserList());

        // đổ dữ liệu từ Adapter lên RecyclerView
        rcvUser.setAdapter(userAdapter);

        return mView;
    }

    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();

        userList.add(new User("User 1"));
        userList.add(new User("User 2"));
        userList.add(new User("User 3"));
        userList.add(new User("User 4"));
        userList.add(new User("User 5"));
        userList.add(new User("User 6"));
        userList.add(new User("User 7"));
        userList.add(new User("User 8"));
        userList.add(new User("User 9"));
        userList.add(new User("User 10"));
        userList.add(new User("User 11"));
        userList.add(new User("User 12"));

        return userList;
    }
}
```

<hr />

## LƯU Ý KHI SỬ DỤNG VIEW BINDING

### IGNORE NHỮNG LAYOUT KHÔNG CẦN SỬ DỤNG VIEW BINDING

- khi bật View Binding trong ``Gradle`` thì View Binding sẽ được áp dụng cho toàn bộ Project
- nếu muốn bỏ qua 1 Layout nào đó không sử dụng View Binding ta thêm thuộc tính ``tools:viewBindingIgnore="true"`` vào Layout đó
- VD
```xml
<LinearLayout
        tools:viewBindingIgnore="true" >
</LinearLayout>
```

### XÁC ĐỊNH KIỂU CHUNG CỦA VIEW

- cùng 1 View, Widget, nhưng ở những file layout riêng biệt, nó được định nghĩa là loại View riêng biệt, thì ta cần xác định kiểu cao nhất cho nó và thêm thuộc tính ``tools:viewBindingType="\[common_base_class]"``
- ví dụ ta có 2 file layout cùng 1 màn hình, nhưng 1 layout là portrait, 1 layout là landscape, cùng 1 widget trên màn hình đó nhưng ở portrait là TextView, ở Landscape thì lại là EditText
- VD
```xml
# in res/layout/example.xml

<TextView android:id="@+id/user_bio" />

# in res/layout-land/example.xml

<EditText android:id="@+id/user_bio" />
```

- lúc này kiểu chung của TextView và EditText là TextView, vì EditText extends TextView, nên ta thêm thuộc tính và widget EditText như sau
```xml
# in res/layout/example.xml (unchanged)

<TextView android:id="@+id/user_bio" />

# in res/layout-land/example.xml

<EditText android:id="@+id/user_bio" tools:viewBindingType="TextView" />
```

- hoặc ta chọn kiểu cao nhất cho cả 2 widget, như trường hợp layout màn hình điện thoại ta có widget là ``BottomNavigationView ``, màn hình tablet là ``NavigationRailView ``, mà kiểu chung cao nhất của cả 2 là ``NavigationBarView``, ta thêm thuộc tính xác định kiểu Binding cho cả 2 widget
```xml
# in res/layout/navigation_example.xml

<BottomNavigationView android:id="@+id/navigation" tools:viewBindingType="NavigationBarView" />

# in res/layout-w720/navigation_example.xml

<NavigationRailView android:id="@+id/navigation" tools:viewBindingType="NavigationBarView" />
```

- tránh trường hợp thiết kế cùng widget hay view mà có kiểu chẳng liên quan đến nhau
- VD
```xml
  <TextView tools:viewBindingType="ImageView" /> <!-- ImageView is not related to TextView. -->

  <TextView tools:viewBindingType="Button" /> <!-- Button is not a superclass of TextView. -->
```

### BINDING LAYOUT VỚI TRƯỜNG HỢP INCLUDE LAYOUT

- trong trường hợp ta sử dụng ``include layout``, nếu có khai báo id cho từng View thì ta vẫn sử dụng ViewBinding để gọi đến các View đó 1 cách bình thường và theo thứ tự
- ví dụ ta có 1 file layout __layout_level2.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/layout_level_2"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:background="@color/black"
    android:orientation="vertical">

</LinearLayout>
```

- ta ``include layout`` này vào __fragment_my.xml__ và vẫn khai báo id cho nó
- __fragment_my.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <include
        android:id="@+id/layout_level_1"
        layout="@layout/layout_level2"/>

</LinearLayout>
```

- và khi dụng ViewBinding trong __MyFragment.java__
```java
public class MyFragment extends Fragment {
    //....

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //....

        // gọi ID 2 lần
        mFragmentMyBinding.layoutLevel1.layoutLevel2.setBackgroundColor(getResources().getColor(R.color.blue, mView.getContext().getTheme()));

        //.../
    }
}
```

### ƯU ĐIỂM SO VỚI CÁCH SỬ DỤNG FIND VIEW BY ID

- Null safety: khi sử dụng View Binding rủi ro xảy ra lỗi ``NullPointerException`` không còn xảy ra nếu như không ánh xạ View với ``findViewById()`` như cách thông thường, và trong View Binding nếu không khai báo id thì sẽ không truy xuất View được
- Type safety: khi sử dụng View Binding ta không cần khai báo kiểu View của biến, chỉ cần gọi Binding class và truy xuất đến Id mà Binding class quản lý


### SO VỚI DATA BINDING

#### ƯU ĐIỂM
- compile nhanh hơn, dễ sử dụng

#### NHƯỢC ĐIỂM
- không hỗ trợ layout variables hoặc layout expression
- không hỗ trợ ánh xạ dữ liệu 2 chiều