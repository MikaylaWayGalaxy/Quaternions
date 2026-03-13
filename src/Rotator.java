//***********************************************************************************************//
// Uses quaternions to rotate vectors (points in 3d space).                                      //
// This class contains all static functions allowing rotation in different ways.                 //
//                                                                                               //
// If you start at the origin and look along the axis of rotation, a positive angle is clockwise //
// rotation. For counter-clockwise rotation, use negative angles. This follows the curled finger //
// right hand rule.                                                                              //
//***********************************************************************************************//
public class Rotator
{
    //rotates a point given an angle of rotation and an axis of rotation
    //see above for rules on the angle and axis
    public static VectorR3 Rotate(VectorR3 Point, double Angle, VectorR3 Axis)
    {
        Quaternion NewPoint = new Quaternion(0.0, Point);           //pure vector quaternion representing the point in 3d space
        Quaternion RotQuat = new Quaternion(Math.cos(Angle / 2.0), Axis.Normalize().Scale(Math.sin(Angle / 2.0)));      //quaternion used to rotate
        NewPoint.MultiplyLeft(RotQuat);
        NewPoint.MultiplyRight(RotQuat.Conjugate());
        return NewPoint.GetVector();                                     //the vector component is the rotated point
    }



    //rotates a point using a rotation quaternion
    //assumes this quaternion has been normalized
    public static VectorR3 Rotate(VectorR3 Point, Quaternion RotQuat)
    {
        Quaternion NewPoint = new Quaternion(0.0, Point);
        NewPoint.MultiplyLeft(RotQuat);
        NewPoint.MultiplyRight(RotQuat.Conjugate());
        return NewPoint.GetVector();
    }



    //finds the rotation quaternion given an angle and an axis of rotation
    //rotation quaternion = cos(theta/2) + sin(theta/2)*v
    //where v is a 3D vector determining the axis of rotation
    //and theta is the angle rotated about that axis
    public static Quaternion RotationQuaternion(double Angle, VectorR3 Axis)
    {
        return new Quaternion(Math.cos(Angle / 2.0), Axis.Normalize().Scale(Math.sin(Angle / 2.0)));
    }



    //breaks a rotation into smaller moves
    //takes in starting point, angle of rotation, axis of rotation, and number of desired moves
    //returns an array of vectors containing points to move along
    public static VectorR3[] SmoothRotate(VectorR3 Point, double Angle, VectorR3 Axis, int NumMoves)
    {
        if (NumMoves < 1)           //input validation: if NumMoves is less than 1, do a single move instead
            NumMoves = 1;
        VectorR3[] MoveArray = new VectorR3[NumMoves];
        Quaternion RotQuat = RotationQuaternion(Angle / NumMoves, Axis);
        for(int i = 0; i < NumMoves; i++)
        {
            MoveArray[i] = Rotate(Point, RotQuat);      //save point to array
            Point = MoveArray[i];                       //update current point
        }
        return MoveArray;
    }



    //rotates in 3d given an angle to rotate around all three standard axes
    //angles result in clockwise rotation, use negative angles for counter-clockwise rotation
    //rotates in order: X -> Y -> Z
    public static VectorR3 RotateXYZ(VectorR3 Point, double AngleX, double AngleY, double AngleZ)
    {
        Quaternion QX = RotationQuaternion(AngleX, VectorR3.IHAT());
        Quaternion QY = RotationQuaternion(AngleY, VectorR3.JHAT());
        Quaternion QZ = RotationQuaternion(AngleZ, VectorR3.KHAT());
        Quaternion RotQuat = Quaternion.Multiply3(QZ, QY, QX);          //rotations are applied right to left
        return Rotate(Point, RotQuat);
    }
    //todo: consider making other versions



    //rotates around all 3 axes smoothly
    //rotates in order: X -> Y -> Z
    //breaks into smaller moves
    //takes in starting point, all 3 angles, and the desired number of moves
    //returns an array of vectors containing points to move along
    public static VectorR3[] SmoothRotateXYZ(VectorR3 Point, double AngleX, double AngleY, double AngleZ, int NumMoves)
    {
        if (NumMoves < 1)           //input validation: if NumMoves is less than 1, do a single move instead
            NumMoves = 1;
        VectorR3[] MoveArray = new VectorR3[NumMoves];
        double RotAngle;
        VectorR3 RotAxis;
        Quaternion QX = RotationQuaternion(AngleX, VectorR3.IHAT());
        Quaternion QY = RotationQuaternion(AngleY, VectorR3.JHAT());
        Quaternion QZ = RotationQuaternion(AngleZ, VectorR3.KHAT());
        Quaternion RotQuat = Quaternion.Multiply3(QZ, QY, QX);          //gets full rotation first
        RotAngle = AngleOfRot(RotQuat) / NumMoves;                      //extracts angle then divides by NumMoves
        RotAxis = AxisOfRot(RotQuat);                                   //extracts rotation axis without modifying
        RotQuat.SetAll(RotationQuaternion(RotAngle, RotAxis));          //rebuilds rotation quaternion with smaller angle
        for(int i = 0; i < NumMoves; i++)
        {
            MoveArray[i] = Rotate(Point, RotQuat);      //save point to array
            Point = MoveArray[i];                       //update current point
        }
        return MoveArray;
    }



    //rotates a point around the x-axis by a given angle clockwise
    public static VectorR3 RotateXAxis(VectorR3 Point, double Angle)
    {
        return Rotate(Point, Angle, VectorR3.IHAT());
    }



    //rotates a point around the y-axis by a given angle clockwise
    public static VectorR3 RotateYAxis(VectorR3 Point, double Angle)
    {
        return Rotate(Point, Angle, VectorR3.JHAT());
    }



    //rotates a point around the z-axis by a given angle clockwise
    public static VectorR3 RotateZAxis(VectorR3 Point, double Angle)
    {
        return Rotate(Point, Angle, VectorR3.KHAT());
    }



    //given a quaternion, returns the angle of rotation if it were to represent a rotation
    public static double AngleOfRot(Quaternion Quat)
    {
        return 2*Math.acos(Quat.GetReal());
    }
    //todo: check this



    //given a quaternion, returns the axis of rotation if it were to repersent a rotation
    public static VectorR3 AxisOfRot(Quaternion Quat)
    {
        return Quat.GetVector();       //may not be normalized
    }
    //todo: check this
}

//todo: rotate and translate function?
