package Quaternion;

//***************************************************************************************************//
// 3-dimensional vectors containing 3 real numbers. Also called R^3.                                 //
// Often represented as: <a, b, c> = ai + bj + ck                                                    //
// where a,b,c are real numbers and i,j,k are the standard basis vectors for R^3.                    //
// They can also represent points in 3d space if the vectors are assumed to start from the origin.   //
//***************************************************************************************************//
public class VectorR3
{
    //***********************************************************************************************//
    //                                      private member variables                                 //
    //***********************************************************************************************//
    private double X;       //x component
    private double Y;       //y component
    private double Z;       //z component



    //***********************************************************************************************//
    //                                             constructors                                      //
    //***********************************************************************************************//
    //no-arg constructor, initialize to <0,0,0>
    public VectorR3()
    {
        X = Y = Z = 0.0;
    }

    //Takes in x, y, z and initalizes
    public VectorR3(double X, double Y, double Z)
    {
        VectorR3.this.X = X;
        VectorR3.this.Y = Y;
        VectorR3.this.Z = Z;
    }

    //copy constructor
    public VectorR3(VectorR3 Original)
    {
        X = Original.GetX();
        Y = Original.GetY();
        Z = Original.GetZ();
    }



    //***********************************************************************************************//
    //                                      accessors and mutators                                   //
    //***********************************************************************************************//
    public double GetX() {return X;}
    public double GetY() {return Y;}
    public double GetZ() {return Z;}
    public void SetX(double X) {VectorR3.this.X = X;}
    public void SetY(double Y) {VectorR3.this.Y = Y;}
    public void SetZ(double Z) {VectorR3.this.Z = Z;}
    public void SetAll(VectorR3 V)
    {
        X = V.X;
        Y = V.Y;
        Z = V.Z;
    }
    public void Copy(VectorR3 V) { SetAll(V); }



    //***********************************************************************************************//
    //                                      public member functions                                  //
    //***********************************************************************************************//
    //checks equality
    //returns true if vectors are equal, otherwise returns false
    public boolean Equals(VectorR3 V)
    {
        if (X != V.X || Y != V.Y || Z != V.Z)
            return false;
        return true;
    }

    //add a vector to this vector and returns the result
    //note: this does modify this vector
    public VectorR3 Add(VectorR3 V)
    {
        X += V.X;
        Y += V.Y;
        Z += V.Z;
        return this;
    }

    //adds a vector (scaled by -1) to this vector
    //note: this does modify this vector
    public VectorR3 Subtract(VectorR3 V)
    {
        X -= V.X;
        Y -= V.Y;
        Z -= V.Z;
        return this;
    }

    //scales by a real numbers
    //aka scalar multiplication
    //note: this does modify this vector
    public VectorR3 Scale(double Scalar)
    {
        X *= Scalar;
        Y *= Scalar;
        Z *= Scalar;
        return this;
    }

    //make unit length
    //divide each component by the vector's magnitude
    //note: this does modify this vector
    public VectorR3 Normalize()
    {
        if (X == 0.0 && Y == 0.0 && Z == 0.0)       //if zero vector, return this without scaling
            return this;                            //todo: consider throwing exception
        return Scale(1.0 / Magnitude());
    }

    //returns magnitude aka length aka rho
    public double Magnitude()
    {
        return Math.sqrt(Dot(this,this));
    }

    //radius in the XY plane
    //used for cylindrical coordinates
    public double RadiusXY()
    {
        return Math.sqrt(Math.pow(X, 2.0) + Math.pow(Y, 2.0));
    }

    //angle theta in the XY plane
    //starting from the positive x-axis, go counter-clockwise until the angle terminates
    public double Theta()
    {
        return Math.atan2(Y, X);
    }

    //angle phi
    //starting from the positive z-axis, rotate about the origin until the angle terminates
    //typically between 0 and pi
    //used for spherical coordinates
    public double Phi()
    {
        return Math.acos(Z / Magnitude());      //todo: check if acos works in all quadrants
    }

    //returns an array with all 3 coords in rectangular coordinates (x, y, z)
    public double[] Rectangular()
    {
        return new double[] {X, Y, Z};
    }

    //returns an array with all 3 coords in cylindrical coordinates (r, theta, z)
    public double[] Cylindrical()
    {
        return new double[] { RadiusXY(), Theta(), Z };
    }

    //returns an array with all 3 coords in spherical coordinates (rho, theta, phi)
    public double[] Spherical()
    {
        return new double[] { Magnitude(), Theta(), Phi()};
    }



    //***********************************************************************************************//
    //                                       static functions                                        //
    //***********************************************************************************************//
    //dot product
    public static double Dot(VectorR3 A, VectorR3 B)
    {
        return (A.X)*(B.X) + (A.Y)*(B.Y) + (A.Z)*(B.Z);
    }

    //cross product
    public static VectorR3 Cross(VectorR3 Left, VectorR3 Right)
    {
        return new VectorR3(Left.Y*Right.Z - Left.Z*Right.Y,
                            Left.Z*Right.X - Left.X*Right.Z,
                            Left.X*Right.Y - Left.Y*Right.X);
    }

    //unit vector in the direction of the x-axis
    public static VectorR3 IHAT()
    {
        return new VectorR3(1.0, 0.0, 0.0);
    }

    //unit vector in the direction of the y-axis
    public static VectorR3 JHAT()
    {
        return new VectorR3(0.0, 1.0, 0.0);
    }

    //unit vector in the direction of the z-axis
    public static VectorR3 KHAT()
    {
        return new VectorR3(0.0, 0.0, 1.0);
    }
}
