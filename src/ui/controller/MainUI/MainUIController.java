package ui.controller.MainUI;

import dao.UserMapper;
import db.DBAccess;
import entity.CurrentUser;
import entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;
import utils.CodeUtils;
import utils.JavaFXHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainUIController  implements Initializable {
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_quit;
    @FXML
    private TextField textField_cardNo;
    @FXML
    private PasswordField passwordField;

    /**
     * 登录
     * @throws IOException
     */
    @FXML
    private void login() throws IOException {
        String cardNo = textField_cardNo.getText();
        String password = CodeUtils.getMD5(passwordField.getText());
        //验证用户是否可用
        SqlSession sqlSession = DBAccess.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(cardNo);
        sqlSession.close();
        if(user==null || !password.equals(user.getPassword())){
            AlertInfoDialog("信息","信息","用户名密码不匹配,请重新输入!");
            passwordField.selectAll();
            passwordField.requestFocus();
            return;
        }
        if(!"正常".equals(user.getAvailable())){
            AlertInfoDialog("信息","信息","用户状态异常,无法登陆系统.\n具体原因:"+user.getAvailable());
            return;
        }
        CurrentUser.userID = user.getId();		//设置全局变量
        CurrentUser.password = user.getPassword();
        CurrentUser.type = user.getType();

        //TODO:判断用户是否勾选了维护信息的checkbox




        //用户验证通过,跳转到相应角色的界面
        if("系统管理员".equals(user.getType())){
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/resources/fxml/SystemAdmin.fxml")));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("系统管理员");
            stage.show();
            JavaFXHelper.getStage().close();
            return;
        }
        if("一卡通管理员".equals(user.getType())){
            return;
        }
        if("学生".equals(user.getType())){
            return;
        }
        if("教学系统管理员".equals(user.getType())){
            return;
        }
        if("图书管理员".equals(user.getType())){
            return;
        }
        if("宿舍管理员".equals(user.getType())){
            return;
        }
        AlertInfoDialog("信息","什么鬼","Opps,发生了未知意外!");
    }


    /**
     * 退出程序
     */
    @FXML
    private void quit(){
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION,"确认退出?",ButtonType.YES, ButtonType.CANCEL).showAndWait();
        if(buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            JavaFXHelper.getStage().close();
        }
    }

    /**
     * 查看帮助
     */
    @FXML
    private void viewHelp(){
        AlertInfoDialog("帮助","帮助","输入正确的用户卡号和密码即可登录\n若需要申请注册卡号,请联系管理员!");
    }

    /**
     * 忘记密码
     */
    @FXML
    private void forgetPassword(){
        AlertInfoDialog("忘记密码","忘记密码","请带上相关身份证明材料到一卡通管理处重置密码。");
    }

    /**
     * 弹出一个对话框
     * @param title
     * @param header
     * @param message
     */
    public void AlertInfoDialog(String title,String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.initOwner(JavaFXHelper.getStage());
        alert.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
