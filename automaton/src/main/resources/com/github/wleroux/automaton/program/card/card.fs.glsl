#version 450 core

in vec2 PosCoord;
uniform int Width;
uniform int Height;
uniform vec4 Color;

struct Radius {
    int topLeft;
    int topRight;
    int bottomRight;
    int bottomLeft;
};

uniform struct {
    vec4 Color;
    int top;
    int right;
    int bottom;
    int left;
    Radius Radius;
} Border;
uniform struct {
    int top;
    int right;
    int bottom;
    int left;
} Margin;


out vec4 color;

vec4 MarginColor = vec4(0f, 0f, 0f, 0f);

bool isMargin(int x, int y);
bool isBorder(int x, int y);
void main()
{
    int realX = int(PosCoord.x * Width);
    int realY = int(PosCoord.y * Height);
    if (isMargin(realX, realY)) {
        color = MarginColor;
    } else if (isBorder(realX, realY)) {
        color = Border.Color;
    } else {
        color = Color;
    }
}

bool isMargin(int x, int y) {
    if (Height - Margin.top <= y || y < Margin.bottom) return true;
    if (Width - Margin.right <= x || x < Margin.left) return true;

    int topLeftX = Margin.left + Border.Radius.topLeft;
    int topLeftY = Height - Margin.top - Border.Radius.topLeft;
    if (x <= topLeftX && y > topLeftY) {
        int d = int(sqrt(pow((x - topLeftX), 2.0) + pow((y - topLeftY), 2.0)));
        return (d > Border.Radius.topLeft);
    }

    int topRightX = Width - Margin.right - Border.Radius.topRight;
    int topRightY = Height - Margin.top - Border.Radius.topRight;
    if (x >= topRightX && y > topRightY) {
        int d = int(sqrt(pow((x - topRightX), 2.0) + pow((y - topRightY), 2.0)));
        return (d > Border.Radius.topRight);
    }

    int bottomRightX = Width - Margin.right - Border.Radius.bottomRight;
    int bottomRightY = Margin.bottom + Border.Radius.bottomRight;
    if (x >= bottomRightX && y < bottomRightY) {
        int d = int(sqrt(pow((x - bottomRightX), 2.0) + pow((y - bottomRightY), 2.0)));
        return (d > Border.Radius.bottomRight);
    }

    int bottomLeftX = Margin.left + Border.Radius.bottomLeft;
    int bottomLeftY = Margin.bottom + Border.Radius.bottomLeft;
    if (x <= bottomLeftX && y <= bottomLeftY) {
        int d = int(sqrt(pow((x - bottomLeftX), 2.0) + pow((y - bottomLeftY), 2.0)));
        return (d > Border.Radius.bottomLeft);
    }

    return false;
}


bool isBorder(int x, int y) {
    if (Height - Margin.top - Border.top <= y || y < Margin.bottom + Border.bottom) return true;
    if (Width - Margin.right - Border.right <= x || x < Margin.left + Border.left) return true;

    int topLeftX = Margin.left + Border.Radius.topLeft + Border.left;
    int topLeftY = Height - Margin.top - Border.Radius.topLeft - Border.top;
    if (x < topLeftX && y > topLeftY) {
      int d = int(sqrt(pow((x - topLeftX), 2.0) + pow((y - topLeftY), 2.0)));
      return (d > Border.Radius.topLeft);
    }

    int topRightX = Width - Margin.right - Border.Radius.topRight - Border.right;
    int topRightY = Height - Margin.top - Border.Radius.topRight - Border.top;
    if (x > topRightX && y > topRightY) {
      int d = int(sqrt(pow((x - topRightX), 2.0) + pow((y - topRightY), 2.0)));
      return (d > Border.Radius.topRight);
    }

    int bottomRightX = Width - Margin.right - Border.Radius.bottomRight - Border.right;
    int bottomRightY = Margin.bottom + Border.Radius.bottomRight + Border.bottom;
    if (x > bottomRightX && y < bottomRightY) {
      int d = int(sqrt(pow((x - bottomRightX), 2.0) + pow((y - bottomRightY), 2.0)));
      return (d > Border.Radius.bottomRight);
    }

    int bottomLeftX = Margin.left + Border.Radius.bottomLeft + Border.left;
    int bottomLeftY = Margin.bottom + Border.Radius.bottomLeft + Border.bottom;
    if (x < bottomLeftX && y < bottomLeftY) {
        int d = int(sqrt(pow((x - bottomLeftX), 2.0) + pow((y - bottomLeftY), 2.0)));
        return (d > Border.Radius.bottomLeft);
    }

    return false;
}