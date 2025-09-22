package org.firstinspires.ftc.TeamCode;


// where it is located
import com qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor

public class LB_Auto {
    //dclaring variables
    private DcMotor leftFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightFrontMotor;
    private DcMotor RightBackMotor;
    private DcMotor arm;


    waitForStart();

  leftFrontMotor.setPower(0.5);
  leftBackMotor.setPower(0.5);
  rightFrontMotor.setPower(0.5);
  rightBackMotor.setPower(0.5);

  leftFrontMotor.setPower(1);
  leftBackMotor.setPower(1);
  rightFrontMotor.setPower(0);
  rightBackMotor.setPower(0);

  arm.setPower(1);
  arm.setPower(-1);
  arm.setPower(1);
  arm.setPower(-1);
  arm.setPower(1);
  arm.setPower(-1);

  leftFrontMotor.setPower(1);
  leftBackMotor.setPower(1);
  rightFrontMotor.setPower(0);
  rightBackMotor.setPower(0);

  leftFrontMotor.setPower(0.5);
  leftBackMotor.setPower(0.5);
  rightFrontMotor.setPower(0.5);
  rightBackMotor.setPower(0.5);

  arm.setPower(1);
  arm.setPower(-1);
  arm.setPower(1);
  arm.setPower(-1);
  arm.setPower(1);
  arm.setPower(-1);








    //go forward to april tag
    //scan april tag
    //rotate 180 degrees
    //repeat 3 times
    //if ball green
    //pick up green ball
    //go forward
    //else
    //pick up purple ball
    //go forward
    //rotate 180 degrees
    //go forward to goal
    //shoot balls one by one into goal
}





