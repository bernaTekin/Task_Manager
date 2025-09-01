// com/jantsa/task/manager/TaskManager/config/AdminSecurityInterceptor.java

package com.jantsa.task.manager.TaskManager.config;

import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // --- GÜVENLİK KONTROLÜ BAŞLADI ---
        System.out.println("\n\n=========================================================");
        System.out.println(">>> ADMIN SECURITY INTERCEPTOR DEVREYE GİRDİ <<<");
        System.out.println(">>> Istek Yapilan URL: " + request.getRequestURI());

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            System.out.println(">>> DURUM: Session bulunamadi veya kullanici giris yapmamis.");
            System.out.println(">>> KARAR: ERISIM ENGELLENDI (401 Unauthorized)");
            System.out.println("=========================================================\n");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Yetkisiz Erisim - Lutfen Giris Yapin");
            return false;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        System.out.println(">>> DURUM: Session bulundu. Giris Yapan Kullanici: " + loggedInUser.getName() + ", Rolu: " + loggedInUser.getUserRole());

        if (loggedInUser.getUserRole() != UserRole.ADMIN) {
            System.out.println(">>> KARAR: ERISIM ENGELLENDI (403 Forbidden) - Cunku kullanici ADMIN degil.");
            System.out.println("=========================================================\n");
            response.sendError(HttpStatus.FORBIDDEN.value(), "Bu alana erisim yetkiniz bulunmamaktadir.");
            return false;
        }

        System.out.println(">>> KARAR: ERISIME IZIN VERILDI - Cunku kullanici ADMIN.");
        System.out.println("=========================================================\n");
        return true;
    }
}