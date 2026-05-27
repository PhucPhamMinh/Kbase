package com.kbase.project.service;

import com.kbase.project.exception.BadRequestException;
import com.kbase.project.model.Project;
import com.kbase.project.model.ProjectInvitation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String frontendBaseUrl;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.from}") String fromAddress,
                        @Value("${app.frontend.base-url}") String frontendBaseUrl) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    public void sendProjectInvitation(Project project, ProjectInvitation invitation) {
        String acceptUrl = frontendBaseUrl + "/invitations/accept?token=" + invitation.getToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(invitation.getInvitedEmail());
        message.setSubject("KBase project invitation: " + project.getProjectName());
        message.setText("""
                You have been invited to join a KBase project.

                Project: %s

                Permissions:
                - Read: %s
                - Add: %s
                - Modify: %s
                - Delete: %s

                Open this link while signed in with this email address to accept the invitation:
                %s

                This invitation expires at %s.
                """.formatted(
                project.getProjectName(),
                yesNo(invitation.getCanRead()),
                yesNo(invitation.getCanAdd()),
                yesNo(invitation.getCanModify()),
                yesNo(invitation.getCanDelete()),
                acceptUrl,
                invitation.getExpiresAt()
        ));
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            throw new BadRequestException("Unable to send invitation email");
        }
    }

    private String yesNo(Boolean value) {
        return Boolean.TRUE.equals(value) ? "Yes" : "No";
    }
}
