export interface MyJwtPayload {
  iss: string;
  sub: string;
  exp: number;
  role: string;
  status: string;
}