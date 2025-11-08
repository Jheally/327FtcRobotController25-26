package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "New Teleop v5")
public class New_Teleop extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor rollerMotor;

    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        rollerMotor = hardwareMap.get(DcMotor.class, "rollerMotor");

        //change directions?
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        rollerMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        if (opModeIsActive()) {

            while (opModeIsActive()) {

                double y = -gamepad1.left_stick_y;
                double x = gamepad1.right_stick_x * 0.7;
                double r = gamepad1.left_stick_x;
                double m = -(gamepad2.right_stick_y * 0.5);
                double b = gamepad2.left_stick_y * 0.65;
                boolean bumperR = gamepad1.right_bumper;
                float triggerR = gamepad1.right_trigger;
                boolean bumperL = gamepad1.left_bumper;

                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1);

                frontLeft.setPower((y + x + r) / denominator);
                backLeft.setPower((y - x + r) / denominator);
                frontRight.setPower((y - x - r) / denominator);
                backRight.setPower((y + x - r) / denominator);

                if (bumperR) {
                    rollerMotor.setPower(1);
                } else{
                    rollerMotor.setPower(0);
                }

                if(bumperL){
                    rollerMotor.setPower(-1);
                } else {
                    rollerMotor.setPower(0);
                }
            }
        }
    }
}
