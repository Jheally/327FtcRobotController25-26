package org.firstinspires.ftc.TeamCode;

public class New_Teleop {


      //variables for motors
      private dcMotor frontLeft;
      private dcMotor frontRight;
      private dcMotor backLeft;
      private dcMotor backRight;

      publci void runOpMode(){
            frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
            frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
            backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
            backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor"); 

            waitforStart();
            //this code wont run until the game starts
            frontLeft.setDirection(DcMotor.Direction.REVERSE);

            if (opModeIsActive()) {

                while (opModeIsActive()) {
                      //makign the variables
                    y = -gamepad1.left_stick_y;
                    x = gamepad1.right_stick_x * 0.7;
                    r = gamepad1.left_stick_x;
                    m = -(gamepad2.right_stick_y * 0.5);
                    b = gamepad2.left_stick_y * 0.65;
                    denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx), Math.abs(m))), 1));
                  
    
                    // moving mecanum wheels based on input
                    frontLeft.setPower((y + x + r) / denominator);
                    backLeft.setPower(((y - x) + r) / denominator);
                    frontRight.setPower(((y - x) - r) / denominator);
                    backRight.setPower(((y + x) - r) / denominator);
                }
      }

     // declaring hardware
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;

    // variables for hardware
    frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
    backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
    frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
    backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
}


