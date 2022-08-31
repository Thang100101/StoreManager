package com.example.yourshopapplication.Activity.Main;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourshopapplication.Activity.Capture.CaptureA;
import com.example.yourshopapplication.Activity.EmployeeManager.EmployeeManagerActivity;
import com.example.yourshopapplication.Activity.GrantPermission;
import com.example.yourshopapplication.Activity.Login.LoginActivity;
import com.example.yourshopapplication.Activity.Payment.PaymentActivity;
import com.example.yourshopapplication.Activity.QuantityProduct.QuantityActivity;
import com.example.yourshopapplication.Adapter.FeatureAdapter;
import com.example.yourshopapplication.Adapter.UserAdapter;
import com.example.yourshopapplication.DAO.PermissionDAO;
import com.example.yourshopapplication.DAO.ProductDAO;
import com.example.yourshopapplication.DAO.UserWithPermissionDAO;
import com.example.yourshopapplication.Database.StoreManagerDatabase;
import com.example.yourshopapplication.Model.Category;
import com.example.yourshopapplication.Model.Currency;
import com.example.yourshopapplication.Model.Feature;
import com.example.yourshopapplication.Model.Permission;
import com.example.yourshopapplication.Model.Product;
import com.example.yourshopapplication.Model.User;
import com.example.yourshopapplication.Activity.Login.LoginFacebook;
import com.example.yourshopapplication.Activity.Login.LoginGoogle;
import com.example.yourshopapplication.Activity.Login.LoginManagerPresenter;
import com.example.yourshopapplication.Model.UserPermissionCrossRef;
import com.example.yourshopapplication.Model.UserPermissionEnum;
import com.example.yourshopapplication.Model.UserWithPermission;
import com.example.yourshopapplication.R;
import com.example.yourshopapplication.Activity.Login.LoginView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainStoreActivity extends AppCompatActivity implements LoginView, LoginLocalView, PermissionManagerView,
        NavigationView.OnNavigationItemSelectedListener, FragmentView {
    private LoginManagerPresenter login;
    private SharedPreferences prefer;
    private ProgressDialog progressDialog;
    private LoginLocalManager loginLocal;
    private Dialog dialogSignUp;
    private StoreManagerDatabase db;
    private PermissionDAO pmsDAO;
    private RecyclerView rclFeature;
    private List<User> localUsers;
    private User currentUser;
    private PermissionManager permissionManager;
    private FeatureAdapter adapter;
    private int countFragment=0;
    private long timeFirstClick=0;
    private Toast toast;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView, navigationLocal;
    private FragmentProductPresenter fragmentPresenter;

    private ActivityResultLauncher<ScanOptions> launcher =registerForActivityResult(new ScanContract(), new ActivityResultCallback<ScanIntentResult>() {
        @Override
        public void onActivityResult(ScanIntentResult result) {
            if(result.getContents() != null){
                openFragmentDetailProduct(result.getContents());
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_store);

        Mapping();
//        createTestCategory();
        customActionBar();
//        checkHaveSignIn();
        if(!loginLocal.haveManager()) {
            Category.createDefaultCategory(this);
            Currency.createDefaultCurrency(this);
            showDialogSignUp();
        }
        getListFeatures();
        addFragmentCategory();
        editNavigationLoginGF();
        editNavigationLoginLocal();
        eventHandler();

    }

    private void createTestCategory() {
        return;
    }


    private void Mapping() {
        prefer = getSharedPreferences("login", MODE_PRIVATE);

        if(prefer.getString("type","").equals("facebook"))
            login = new LoginFacebook(this,this);
        else
            login = new LoginGoogle(this, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.wait));
        rclFeature = findViewById(R.id.rclview_feature);
        localUsers = new ArrayList<>();
        loginLocal = new LoginLocalManager(this);
        dialogSignUp = new Dialog(this);
        db = StoreManagerDatabase.getInstance(this);
        pmsDAO = db.getPermissionDAO();
        permissionManager = new PermissionManager(this);
        adapter = new FeatureAdapter();
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        navigationLocal = findViewById(R.id.navigation_local);
        fragmentPresenter = new FragmentProductPresenter(this, getSupportFragmentManager());

    }

    private void addFragmentCategory() {
        if(currentUser != null)
            fragmentPresenter.loadMainFragment();
        else
            fragmentPresenter.removeFragment();
    }
    public void goToDetailFragment(Product product){
        fragmentPresenter.loadDetailFragment(product, currentUser);
    }

    public void goToListProductForCategoryFragment(Category category){
        fragmentPresenter.loadProductByCategoryFragment(category);
    }

    private void customActionBar() {
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
        R.string.open_navigation, R.string.close_navigation);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void editNavigationLoginGF(){
        View headerView = navigationView.getHeaderView(0);
        ImageView imgAvatar = headerView.findViewById(R.id.img_avatar);
        TextView txtName = headerView.findViewById(R.id.txt_name);
        TextView txtEmail = headerView.findViewById(R.id.txt_email);
        FirebaseUser user = login.getUser();
        Menu menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        if(user==null) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            return;
        }else {
            Picasso.get().load(user.getPhotoUrl()).into(imgAvatar);
            txtName.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
            menu.getItem(2).setVisible(false);
        }
    }

    private TextView txtName;
    private TextView txtPosition;

    private void editNavigationLoginLocal(){
        View headerView = navigationLocal.getHeaderView(0);
        txtName = headerView.findViewById(R.id.txt_name);
        txtPosition = headerView.findViewById(R.id.txt_position);
        if(currentUser == null){
            txtName.setText("Chưa đăng nhập");
            txtPosition.setText("");
            return;
        }
    }

    private void reLoadNavigationLocal(){
        if(currentUser == null){
            txtName.setText("Chưa đăng nhập");
            txtPosition.setText("");
            return;
        }
        txtName.setText(currentUser.getName());
        UserWithPermissionDAO userWithPermissionDAO = db.getUserWithPermissionDAO();
        UserWithPermission uwp = userWithPermissionDAO.get(currentUser.getId());
        for(Permission p : uwp.getPermissions()){
            if(p.getType().equals(UserPermissionEnum.Admin)) {
                txtPosition.setText("Admin");
                return;
            }
        }
        txtPosition.setText("Staff");
    }

    private void eventHandler(){
        adapter.setItemClickListener(new FeatureAdapter.onItemClickListener() {
            @Override
            public void onClickItem(Feature feature) {
                runFeature(feature);
            }
        });
    }

    private void runFeature(Feature feature){
        switch (feature.getType()){
            case NewProduct:
                fragmentPresenter.loadNewProductFragment();
                break;
            case NewCategory:
                fragmentPresenter.loadNewCategoryFragment();
                break;
            case Payment:
                Intent intent = new Intent(MainStoreActivity.this, PaymentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("employee", currentUser);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
            case ScanCode:
                ScanOptions options = new ScanOptions();
                options.setPrompt("Scan Product");
                options.setBeepEnabled(true);
                options.setOrientationLocked(true);
                options.setCaptureActivity(CaptureA.class);
                grantPermission(options);
                break;
            case AddQuantityProduct:
                Intent intent1 = new Intent(MainStoreActivity.this, QuantityActivity.class);
                startActivity(intent1);
                break;
            case CheckOut:
                loginLocal.signOut(currentUser);
                break;
            case EmployeeManager:
                Intent intentEmployee = new Intent(MainStoreActivity.this, EmployeeManagerActivity.class);
                startActivity(intentEmployee);
                break;
            case CheckIn:
                openDialogSignIn();
                break;
            case ChangeUser:
                openBottomSheetDialog();
                break;
        }
    }

    private void grantPermission(ScanOptions options){
        GrantPermission.scanQRCode(launcher, options, this);
    }

    private void getListFeatures() {
        List<Feature> features = permissionManager.getFeatureForUserPermission(currentUser);
        if(features==null)
            return;
        adapter.setFeatures(features);
        rclFeature.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rclFeature.setLayoutManager(linearLayoutManager);
    }

    private void showDialogSignUp() {
        dialogSignUp.setContentView(R.layout.dialog_sign_up);
        TextView txtTitle = dialogSignUp.findViewById(R.id.txt_title);
        EditText editName = dialogSignUp.findViewById(R.id.edit_name);
        EditText editPass = dialogSignUp.findViewById(R.id.edit_pass);
        EditText editConfirm = dialogSignUp.findViewById(R.id.edit_confirm_pass);
        Button btnSubmit = dialogSignUp.findViewById(R.id.btn_submit);
        Button btnCancel = dialogSignUp.findViewById(R.id.btn_cancel);

        if(!loginLocal.haveManager()) {
            dialogSignUp.setCanceledOnTouchOutside(false);
            txtTitle.setText(getString(R.string.signup_manager));
        }
        dialogSignUp.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSignUp.dismiss();
                progressDialog.show();
                if(!loginLocal.haveManager()) {
                    loginLocal.signUp(editName.getText().toString(),
                            editPass.getText().toString(),
                            editConfirm.getText().toString(), LoginLocalManager.SignUpType.Admin);
                }
                else{
                    loginLocal.signUp(editName.getText().toString(),
                            editPass.getText().toString(),
                            editConfirm.getText().toString(), LoginLocalManager.SignUpType.Staff);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainStoreActivity.this, getString(R.string.have_to_sign_up_manager), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Dialog dialogSignIn;

    private void openDialogSignIn(){
        dialogSignIn = new Dialog(this);
        dialogSignIn.setContentView(R.layout.dialog_sign_up);
        TextView txtTitle = dialogSignIn.findViewById(R.id.txt_title);
        EditText editName = dialogSignIn.findViewById(R.id.edit_name);
        EditText editPass = dialogSignIn.findViewById(R.id.edit_pass);
        EditText editConfirm = dialogSignIn.findViewById(R.id.edit_confirm_pass);
        Button btnSubmit = dialogSignIn.findViewById(R.id.btn_submit);
        Button btnCancel = dialogSignIn.findViewById(R.id.btn_cancel);

        txtTitle.setText("Đăng nhập");
        editConfirm.setVisibility(View.GONE);

        dialogSignIn.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSignIn.dismiss();
                progressDialog.show();
                loginLocal.signIn(editName.getText().toString(), editPass.getText().toString());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSignIn.dismiss();
            }
        });
    }

    private void openBottomSheetDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.bottom_sheet_change_user);
        List<User> users = loginLocal.getLocalUsers();

        List<User> userNew = new ArrayList<>();
        for(User u : users){
            if(u.getId() != currentUser.getId()){
                userNew.add(u);
            }
        }

        RecyclerView rclUser = dialog.findViewById(R.id.rclview_employee);
        UserAdapter adapter = new UserAdapter(this, userNew);
        adapter.setListener(new UserAdapter.onChangeClickListener() {
            @Override
            public void onClick(User user) {
                changeCurrentUser(user);
                dialog.dismiss();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rclUser.setLayoutManager(layoutManager);
        rclUser.setAdapter(adapter);
        dialog.show();
    }

    private void changeCurrentUser(User user){
        currentUser = user;
        reLoadNavigationLocal();
        getListFeatures();
        Toast.makeText(this, "Đổi tài khoản thành công", Toast.LENGTH_SHORT).show();
    }

    private void openFragmentDetailProduct(String code){
        ProductDAO productDAO = db.getProductDAO();
        Product product = productDAO.getByCode(code);
        if(product != null){
            fragmentPresenter.loadDetailFragment(product, currentUser);
        }else{
            Toast.makeText(this, "Không tìm thấy sản phẩm nào", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkHaveSignIn(){
        loginLocal.reSignIn();
    }

    @Override
    public void signInSuccess(String type) {

    }

    @Override
    public void signInFail() {

    }

    @Override
    public void signOutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        Toast.makeText(this, getString(R.string.signout_success), Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void signOutFail() {

    }

    @Override
    public void connectFail() {

    }


    @Override
    public void signInLocalSuccess(User user) {
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        currentUser = user;
        getListFeatures();
        reLoadNavigationLocal();
        progressDialog.dismiss();
        addFragmentCategory();
    }

    @Override
    public void signInLocalFail() {
        Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
        dialogSignIn.show();
        progressDialog.dismiss();
    }

    @Override
    public void signOutLocalSuccess(User nextUser) {
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        currentUser = nextUser;
        getListFeatures();
        reLoadNavigationLocal();
        addFragmentCategory();
    }

    @Override
    public void signOutLocalFail() {

    }

    @Override
    public void changeFragmentSuccess() {
        countFragment++;
    }

    @Override
    public void alreadySignInThisUser(User user) {
        Toast.makeText(this, "Tài khoản "+user.getName()+" đã đăng nhập", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void signUpFail(LoginLocalManager.SignUpFailType type) {
        switch (type){
            case DuplicateName:
                Toast.makeText(this, getString(R.string.duplicate_user), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogSignUp.show();
                break;
            case ConfirmPassWrong:
                Toast.makeText(this, getString(R.string.confirm_not_correct), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogSignUp.show();
                break;
            case Empty:
                Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogSignUp.show();
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        FirebaseUser user = login.getUser();
        MenuItem item = menu.getItem(0);
        if(user==null)
            item.setIcon(R.drawable.icon_un_login);
        else
            item.setIcon(R.drawable.icon_have_login);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_login_status:
                drawer.openDrawer(GravityCompat.END);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signout:
                login.signOut();
                break;
            case R.id.menu_signin:
                Intent intent = new Intent(MainStoreActivity.this, LoginActivity.class);
                intent.putExtra("signin",true);
                startActivity(intent);
                this.finish();
                break;
            case R.id.menu_upload:
                Toast.makeText(this, "click upload", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(countFragment==0){
            if(System.currentTimeMillis() - timeFirstClick <=2000) {
                toast.cancel();
                super.onBackPressed();
            }
            else{
                timeFirstClick = System.currentTimeMillis();
                toast = Toast.makeText(this, "Bấm lần nữa để thoát", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            countFragment--;
            super.onBackPressed();
        }

    }



}